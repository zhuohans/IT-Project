const express = require('express');
const router = express.Router();
const plantCollectionService = require("../service/plantCollection");

const result = (code = 500, msg = "", data = {}) => {
    return {
        code: code,
        msg: msg,
        data: data
    };
};

/**
 * Add plant to collection
 */
router.post("/create", async (req,res) => {
  try {
    const plantCollection = { ...req.body, user_id: req.user.userid };
    await plantCollectionService.insertPlantCollection(plantCollection);
    return res.json(result(code=201, msg="ok"));
  } catch (error) {
    return res.json(error);
  }
});

/**
 * Query all the plant collection information
 */
router.get("/list", async (req, res) => {
  const plantCollections = await plantCollectionService.getCollectionByUser(req.user.userid);
  return res.json(result(code=200, msg="ok", { plantCollections }));
});


module.exports = router;