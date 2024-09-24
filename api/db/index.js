const mysql = require('mysql2/promise');  
  
const db = mysql.createPool({  
  connectionLimit: 10,  
  host: '127.0.0.1',  
  user: 'root',  
  password: '780198',  
  database: 'gardening_advice'  
});  
  
const query = async (sql, values = []) => {  
  try {  
    const [rows, fields] = await db.execute(sql, values);  
    return rows;  
  } catch (error) {  
    throw error;  
  }  
};  
  
const select = async (table, whereObj = {}) => {  
  let sql = `SELECT * FROM ${table}`;  
  let values = [];  
  if (Object.keys(whereObj).length > 0) {  
    const conditions = [];  
    const placeholders = [];  
    for (const key in whereObj) {  
      conditions.push(`${key} = ?`);  
      placeholders.push(whereObj[key]);  
    }  
    sql += ` WHERE ${conditions.join(' AND ')}`;  
    values = placeholders;  
  }  
  return query(sql, values);  
};  
  
const insert = async (table, data) => {  
  const keys = Object.keys(data);  
  const placeholders = keys.map(() => '?').join(', ');  
  const sql = `INSERT INTO ${table} (${keys.join(', ')}) VALUES (${placeholders})`;  
  return query(sql, Object.values(data));  
};  
  
const update = async (table, data, whereObj) => {  
  const setClause = Object.keys(data).map(key => `${key} = ?`).join(', ');  
  const conditions = [];  
  const placeholders = [];  
  for (const key in whereObj) {  
    conditions.push(`${key} = ?`);  
    placeholders.push(whereObj[key]);  
  }  
  const sql = `UPDATE ${table} SET ${setClause} WHERE ${conditions.join(' AND ')}`;  
  const allValues = [...Object.values(data), ...placeholders];  
  return query(sql, allValues);  
};  
  
const remove = async (table, whereObj) => {  
  const conditions = [];  
  const placeholders = [];  
  for (const key in whereObj) {  
    conditions.push(`${key} = ?`);  
    placeholders.push(whereObj[key]);  
  }  
  const sql = `DELETE FROM ${table} WHERE ${conditions.join(' AND ')}`;  
  return query(sql, placeholders);  
};  
  
module.exports = {  
  query,  
  select,  
  insert,  
  update,  
  remove,
  db,
};