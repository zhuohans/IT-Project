const express = require('express');
const categoryService = require("../service/category");
const router = express.Router();

const result = (code = 500, msg = "", data = {}) => {
  return {
    code: code,
    msg: msg,
    data: data
  };
};

/**
 * add plant category
 */
 router.post('/create', async (req, res) => {
  const data = req.body;
  try {
    await categoryService.insertCategories(data);
    return res.json(result(code=201, msg="ok"));
  } catch (e) {
    return res.json(result(msg = e));
  }
});

/**
 * Get all the plant categories of the current user
 */
router.get("/list", async (req, res) => {
  const categories = await categoryService.getCategories();
  if (categories.length > 0) {
    return res.json(result(code=200, msg="ok", data=categories));
  }
  return res.json(result(msg="Fetch plant categories failed."));
});

module.exports = router;
