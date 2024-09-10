const express = require('express');
const router = express.Router();
const eventService = require("../service/event");

const result = (code = 500, msg = "", data = {}) => {
    return {
        code: code,
        msg: msg,
        data: data
    };
};

/**
 * Add event
 */
router.post("/create", async (req,res) => {
  try {
    const event = { ...req.body, user_id: req.user.userid };
    console.log("event: ", event);
    await eventService.insertEvent(event);
    return res.json(result(code=201, msg="ok"));
  } catch (error) {
    return res.json(result(msg=error));
  }
});

/**
 * Query all the events by the user id
 */
router.get("/list", async (req, res) => {
  const { userid } = req.user;
  const events = await eventService.getEventByCreator(userid);
  return res.json(result(code=200, msg="ok", { events }));
});

/**
 * Delete the event by the id
 */
router.delete("/:id/delete", async (req, res) => {
  await eventService.removeEvent(req.params.id);
  return res.json(result(code=200, msg="ok"));
});

module.exports = router;