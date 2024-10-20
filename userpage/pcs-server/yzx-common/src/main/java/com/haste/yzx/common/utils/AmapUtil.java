package com.haste.yzx.common.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

public class AmapUtil {
    private static final String locationApi = "https://restapi.amap.com/v3/ip?output=json&key=db198e3a9bc5349ad72fb28632fef1fc&ip=";
    private static final String weatherApi = "https://restapi.amap.com/v3/weather/weatherInfo?output=json&key=db198e3a9bc5349ad72fb28632fef1fc&extensions=base&city=";
    private static final String key = "";

    /**
     * 根据ip获取位置信息
     */
    public static Map<String, Object> getGeoLocation(String ip) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(locationApi+ip, Map.class);
    }

    public static Map<String, Object> getWeatherByAcode(String acode) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(weatherApi+acode, Map.class);
    }

    public static Object getWeatherByIp(String ip) {
        Map<String, Object> geoLocation = getGeoLocation(ip);
        if (!StrUtil.equals(MapUtil.getStr(geoLocation,"status"),"1")) {
            throw new RuntimeException("获取天气信息失败");
        }
        Map<String, Object> weather = getWeatherByAcode(MapUtil.getStr(geoLocation, "adcode"));
        if (!StrUtil.equals(MapUtil.getStr(weather,"status"),"1")){
            throw new RuntimeException("获取天气信息失败");
        }
        return weather.get("lives");
    }
}