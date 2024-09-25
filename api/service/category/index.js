const db = require("../../db");

const getCategories = async () => {
  return await db.select("plant_categories");
}

const insertCategories = async (category) => {
  await db.insert("plant_categories", category);
}

module.exports = {
  getCategories,
  insertCategories,
}