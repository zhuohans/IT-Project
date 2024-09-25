const db = require("../../db");

const insertPlant = async (plant) => {
  await db.insert("plants", plant);
}

const getPlants = async () => {
  return await db.select("plants");
}

const getPlantById = async (plantId) => {
  const sql = "select * from plants where id = ?";
  return await db.query(sql, [plantId]);
}

const getPlantsByCategory = async (categoryId) => {
  const sql = "select * from plants where plant_category_id = ?";
  return await db.query(sql, [categoryId]);
}

module.exports = {
  insertPlant,
  getPlants,
  getPlantById,
  getPlantsByCategory,
}