const express = require('express');
const multer = require('multer');
const path = require('path');
const router = express.Router();
const postService = require("../service/post");

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
      cb(null, 'public/uploads/posts');  
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
router.post("/create", upload.array('images'), async (req,res) => {
  try {
    const post = { ...req.body, creator_id: req.user.userid };
    let imagePaths = [];
    const files = req.files;
    if (files && files.length > 0) {
      for (const file of files) {
        const filePath = path.join('..', file.path).replace(/\\/g, '/');
        imagePaths.push(filePath);
      }
    }
    // plant.plant_image = filePath;
    // console.log("plant: ", plant);
    const returnPost = await postService.insertPost(post, imagePaths);
    return res.json(result(code=201, msg="ok", { returnPost }));
  } catch (error) {
    return res.json(error);
  }
});

/**
 * Query all the posts
 */
router.get("/list", async (req, res) => {
  const posts = await postService.getPosts();
  return res.json(result(code=200, msg="ok", { posts }));
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