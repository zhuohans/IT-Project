// Import the database module
const db = require("../../db");

// Get user information by username
const getUserByUsername = async (username) => {
  return db.select("users", { username });
};

// Get user information by email
const getUserByEmail = async (email) => {
  return db.select("users", { email });
};

// Insert a new user into the database
const insertUser = async (user) => {
  await db.insert("users", user);
}

// Update user information based on specific criteria
const updateUser = async (data, whereObj) => {
  return await db.update("users", data, whereObj);
}

// Export functions for external use
module.exports = {
  getUserByUsername,
  getUserByEmail,
  insertUser,
  updateUser
}