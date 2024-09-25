const db = require("../../db");

const getUserByUsername = async (username) => {
  return db.select("users", { username });
};

const getUserByEmail = async (email) => {
  return db.select("users", { email });
};

const insertUser = async (user) => {
  await db.insert("users", user);
}

const updateUser = async (data, whereObj) => {
  return await db.update("users", data, whereObj);
}

module.exports = {
  getUserByUsername,
  getUserByEmail,
  insertUser,
  updateUser
}