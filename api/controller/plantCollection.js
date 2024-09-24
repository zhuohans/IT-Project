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
    // console.log("user: ", req.user);
    const plantCollection = { ...req.body, user_id: req.user.userid };
    const collections = await plantCollectionService.getOneCollection(plantCollection.user_id, plantCollection.plant_id);
    if (collections && collections.length > 0) {
      return res.json(result(code=500, msg="You had stored this plant."));
    }
    await plantCollectionService.insertPlantCollection(plantCollection);
    return res.json(result(code=201, msg="You had added this plant to your collection."));
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

/**
 * Check whether the plant has been stored
 */
router.get("/getPlantStatus/:plantId", async (req, res) => {
  // console.log("user: ", req.user);
  const plantCollections = await plantCollectionService.getOneCollection(req.user.userid, req.params.plantId);
  if (plantCollections && plantCollections.length > 0) {
    return res.json(result(code=200, msg="ok", { plantCollections }));
  }
  return res.json(result(code=500, msg="Not be stored."));
});

/**
 * Remove plant collection by id
 */
router.delete("/:id", async (req, res) => {
  await plantCollectionService.removePlantCollection(req.params.id);
  return res.json(result(code=200, msg="Success."));
});


module.exports = router;