package com.haste.yzx.common.utils;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public final class RedisUtil {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    // =============================common============================

    /**
     * set key，value with expiration time
    */
    public void setExKeyValue(String key,Integer value,long time){
        redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    /**
     * specifies the cache expiration time
     * @param key  
     * @param time time in second
     */
    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get expiration time from the key
     * @param key not null
     * @return time in second 0:valid
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }


    /**
     * Verify if key exists
     * @param key 
     * @return true:exists false:not exists
     */
    public boolean hasKey(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * Delete cache
     * @param key one or more values being uploaded
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }


    // ============================String=============================

    /**
     * Fetch normal cache
     * @param key 
     * @return value
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * Normal cache replacement
     * @param key   
     * @param value 
     * @return true:success false:fail
     */

    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * With time setup
     * @param key   
     * @param value 
     * @param time  time>0, if time<=0, time undefined
     * @return true:success false:fail
     */

    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * increment
     * @param key   
     * @param delta increment amount(>0)
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("increment must >0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }


    /**
     * degression
     * @param key   
     * @param delta dgression amount(<0)
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("degression must <0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }


  // ================================Map=================================

    /**
     * HashGet - Retrieve a value from a hash
     * @param key  not null - The key of the hash
     * @param item not null - The field within the hash
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(key, item);
    }

    /**
     * Get all key-value pairs for the given hash key
     * @param key The key of the hash
     * @return A map containing all the key-value pairs
     */
    public Map<Object, Object> hmget(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * HashSet - Store multiple key-value pairs in a hash
     * @param key The key of the hash
     * @param map A map of key-value pairs to store in the hash
     */
    public boolean hmset(String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet - Store multiple key-value pairs in a hash and set expiration time
     * @param key  The key of the hash
     * @param map  A map of key-value pairs to store in the hash
     * @param time Expiration time in seconds
     * @return true if successful, false otherwise
     */
    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Add a key-value pair to a hash, creating the hash if it does not exist
     *
     * @param key   The key of the hash
     * @param item  The field within the hash
     * @param value The value to set
     * @return true if successful, false otherwise
     */
    public boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Add a key-value pair to a hash with an expiration time, creating the hash if it does not exist
     *
     * @param key   The key of the hash
     * @param item  The field within the hash
     * @param value The value to set
     * @param time  Expiration time in seconds (if the hash already has an expiration, this will overwrite it)
     * @return true if successful, false otherwise
     */
    public boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete fields from a hash
     *
     * @param key  The key of the hash (not null)
     * @param item The fields to delete (can be multiple, not null)
     */
    public void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * Check if a field exists in a hash
     *
     * @param key  The key of the hash (not null)
     * @param item The field to check (not null)
     * @return true if the field exists, false otherwise
     */
    public boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * Increment the value of a field in a hash. If the field does not exist, it will be created.
     *
     * @param key  The key of the hash
     * @param item The field to increment
     * @param by   The amount to increment by (greater than 0)
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * Decrement the value of a field in a hash
     *
     * @param key  The key of the hash
     * @param item The field to decrement
     * @param by   The amount to decrement by (less than 0)
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    // ============================set=============================

    /**
     * Get all values from a Set
     * @param key The key of the Set
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Check if a value exists in a Set
     *
     * @param key   The key of the Set
     * @param value The value to check for
     * @return true if the value exists, false otherwise
     */
    public boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Add values to a Set
     *
     * @param key    The key of the Set
     * @param values The values to add (can be multiple)
     * @return The number of values added
     */
    public long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Add values to a Set and set expiration time
     *
     * @param key    The key of the Set
     * @param time   Expiration time in seconds
     * @param values The values to add (can be multiple)
     * @return The number of values added
     */
    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(key, values);
            if (time > 0)
                expire(key, time);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Get the size of a Set
     *
     * @param key The key of the Set
     */
    public long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Remove specific values from a Set
     *
     * @param key    The key of the Set
     * @param values The values to remove (can be multiple)
     * @return The number of values removed
     */
    public long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ===============================list=================================

    /**
     * Get values from a List
     *
     * @param key   The key of the List
     * @param start The start index
     * @param end   The end index (0 to -1 means all values)
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Get the size of a List
     *
     * @param key The key of the List
     */
    public long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * Get a value from a List by index
     *
     * @param key   The key of the List
     * @param index The index (index >= 0: 0 is the first element, 1 is the second, and so on; index < 0: -1 is the last element, -2 is the second last, and so on)
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

    /**
     * Add a value to a List
     *
     * @param key   The key of the List
     * @param value The value to add
    */
