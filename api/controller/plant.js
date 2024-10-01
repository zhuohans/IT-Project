const express = require('express');
const multer = require('multer');
const path = require('path');
const router = express.Router();
const plantService = require("../service/plant");

const result = (code = 500, msg = "", data = {}) => {
  return {
    code: code,
    msg: msg,
    data: data
  };
};


/**
 * upload user avatar
 */  
const storage = multer.diskStorage({  
  destination: function (req, file, cb) {  
      cb(null, 'public/uploads/plants');  
  },  
  filename: function (req, file, cb) {  
      // generating unique file name
      const uniqueSuffix = Date.now() + '-' + Math.round(Math.random() * 1E9);  
      cb(null, file.fieldname + '-' + uniqueSuffix + path.extname(file.originalname));  
  }  
});  

const upload = multer({ storage: storage }); 
/**
 * Add plant
 */
router.post("/create", upload.single('image'), async (req,res) => {
  try {
    const plant = { ...req.body };
    const file = req.file;
    const filePath = file.path.replace(/\\/g, '/').split("public")[1];
    plant.plant_image = filePath;
    console.log("plant: ", plant);
    await plantService.insertPlant(plant);
    return res.json(result(code=201, msg="ok"));
  } catch (error) {
    return res.json(error);
  }
});

/**
 * Query all the plants
 */
router.get("/list", async (req, res) => {
  const plants = await plantService.getPlants();
  return res.json(result(code=200, msg="ok", { plants }));
});

/**
 * Get plant detail by id
 */
router.get("/:id", async (req, res) => {
  const plant = await plantService.getPlantById(req.params.id);
  return res.json(result(code=200, msg="ok", { plant }));
});

/**
 * Get all plants under one category
 */
router.get("/listByCategory/:categoryId", async (req, res) => {
  const plants = await plantService.getPlantsByCategory(req.params.categoryId);
  return res.json(result(code=200, msg="ok", { plants }));
});

module.exports = router;