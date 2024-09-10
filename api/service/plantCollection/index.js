const db = require("../../db");

const insertPlantCollection = async (collection) => {
  await db.insert("plant_collections", collection);
}

const getCollectionByUser = async (userId) => {
  const sql = "select p.* from plants p, plant_collections pc where pc.plant_id = p.id and pc.user_id = ?";
  return await db.query(sql, [userId]);
}

module.exports = {
  insertPlantCollection,
  getCollectionByUser,
}