const db = require("../../db");

const getEventByCreator = async (creatorId) => {
  const sql = "select * from events where user_id = ?"
  return await db.query(sql, [creatorId]);
}

const insertEvent = async (event) => {
  await db.insert("events", event);
}

const removeEvent = async (eventId) => {
  await db.remove("events", { id: eventId });
}

module.exports = {
  getEventByCreator,
  insertEvent,
  removeEvent,
}