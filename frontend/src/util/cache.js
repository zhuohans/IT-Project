/**
 * Local cache tool
 */


export const set = (key, val) => {
  localStorage.setItem(key, val);
}

export const get = (key) => {
  return localStorage.getItem(key);
}

export const remove = (key) => {
  localStorage.removeItem(key);
}

export const clear = () => {
  localStorage.clear();
}

export const setObejct = (key, val) => {
  set(key, JSON.stringify(val));
}

export const getObject = (key) => {
  return JSON.parse(get(key));
}