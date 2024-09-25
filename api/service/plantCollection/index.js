const db = require("../../db");

const insertPlantCollection = async (collection) => {
  await db.insert("plant_collections", collection);
}

const getCollectionByUser = async (userId) => {
  const sql = "select p.* from plants p, plant_collections pc where pc.plant_id = p.id and pc.user_id = ?";
  return await db.query(sql, [userId]);
}

const removePlantCollection = async (plantCollectionId) => {
  return await db.remove("plant_collections", { id: plantCollectionId });
}

module.exports = {
  insertPlantCollection,
  getCollectionByUser,
  removePlantCollection
}