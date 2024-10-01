const express = require('express');
const multer = require('multer'); 
const path = require('path');  
const fs = require('fs').promises;
const fsConstants = require('fs').constants;
const router = express.Router();
const { sign, encript } = require("../util/encrypt");
const userService = require("../service/user");

const result = (code = 500, msg = "", data = {}) => {
  return {
    code: code,
    msg: msg,
    data: data
  };
};

/**
 * user register
 */
 router.post('/register', async (req, res) => {
  const data = req.body;
  let user = await userService.getUserByUsername(data.username);
  // console.log("user: ", user);
  if (user && user.length > 0) {
    return res.json(result(code=500, msg = "The user name has already exist."));
  }
  user = await userService.getUserByEmail(data.email);
  if (user.length > 0) {
    return res.json(result(code=500, msg = "The email has already exist."));
  }
  try {
    // set the default user avatar
    data.avatar = "\\uploads\\avatar\\avatar.jpeg"
    data.password = encript(data.password);
    await userService.insertUser(data);
    return res.json(result(code=200, msg="ok"));
  } catch (e) {
    return res.json(result(code=500, msg = e));
  }
});

/**
 * user login
 */
router.post('/login', async (req, res) => {
  const { username, password } = req.body;
  const returnResult = result(code= 500, msg="User name or password is incorrect");
  const users = await userService.getUserByUsername(username);
  if (users.length > 0) {
    const user = users[0];
    if (user.password == encript(password)) {
        returnResult.code = 200;
        returnResult.msg = "ok";
        returnResult.data = {
            userid: user.id,
            username: user.username,
            email: user.email,
            token: sign({ userid: user.id, username: user.username, email: user.email })
        };
    }
  }
  console.log(returnResult);
  return res.json(returnResult);
});

/**
 * update personal profile information
 */
router.put('/edit', async (req, res) => {
  const data = req.body;
  const currentUser = req.user;
  let users = await userService.getUserByUsername(currentUser.username);
  console.log(currentUser, users);
  if (users.length == 0) {
    return res.json(result(msg = "The user does not exist."));
  }
  let updateMap = {};
  if (data.age) {
    updateMap['age'] = data.age;
  }
  if (data.gender) {
    updateMap['gender'] = data.gender;
  }
  if (data.phoneNumber) {
    updateMap['phone_number'] = data.phoneNumber;
  }
  if (data.address) {
    updateMap['address'] = data.address;
  }
  const updatedUser = await userService.updateUser(  
    updateMap, 
    { id: users[0].id } 
  ); 
  if (!updatedUser) {
    return res.json(result(msg = "Update profile failed."));
  }

  return res.json(result(200, "ok", "Update profile successfully."));
});

/**
 * reset password
 */
router.put('/resetPassword', async (req, res) => {
  const currentUser = req.user;
  const users = await userService.getUserByUsername(currentUser.username);
  if (users.length == 0) {
    return res.json(result(msg = "The user does not exist."));
  }
  const user = users[0];
  const encriptPwd = encript(req.body.password);
  if (encriptPwd != user.password) {
    return res.json(result(msg = "Your original password is invalid."));
  }
  // ensure the new password is not the same to the previous password
  const newEncriptedPwd = encript(req.body.newPassword);
  if (newEncriptedPwd == user.password) {
    return res.json(result(msg = "Your new password is same to your original password."));
  }
  
  const updatedUser = await userService.updateUser({ password: newEncriptedPwd }, { id: user.id }); 
  // console.log("update: ", updatedUser);
  if (!updatedUser) {
    return res.json(result(msg = "Reset password failed."));
  }

  return res.json(result(200, "ok", "Reset password successfully."));
});

/**
 * Get user profile
 */
router.get("/profile", async (req, res) => {
  const users = await userService.getUserByUsername(req.user.username);
  if (users.length > 0) {
    let user = users[0];
    delete user['password'];
    return res.json(result(code=200, msg="ok", { user }));
  }
  return res.json(result(msg="Fetch user profile failed."));
});


/**
 * upload user avatar
 */
const storage = multer.diskStorage({  
  destination: function (req, file, cb) {  
      cb(null, 'public/uploads/avatar');  
  },  
  filename: function (req, file, cb) {  
      const uniqueSuffix = Date.now() + '-' + Math.round(Math.random() * 1E9);  
      cb(null, file.fieldname + '-' + uniqueSuffix + path.extname(file.originalname));  
  }  
});  

async function checkFileExists(filePath) {  
  try {  
      await fs.access(filePath, fsConstants.F_OK);  
      return true;   
  } catch (err) {  
      if (err.code === 'ENOENT') {  
          return false; 
      }  
      throw err;   
  }  
} 
const upload = multer({ storage: storage }); 
router.post('/upload', upload.single('avatar'), async (req, res) => {
  const currentUser = req.user;
  const users = await userService.getUserByUsername(currentUser.username);
  const file = req.file;  
  if (!file) {  
      return res.status(400).send('No file was uploaded.');  
  }
  if (users.length > 0) {
    const avatarPath = path.join(__dirname, users[0].avatar);
    if (await checkFileExists(avatarPath)) {
      await fs.unlink(avatarPath);
    } 
    const filePath = file.path.replace(/\\/g, '/').split("public")[1]; 
    const updateUser = await userService.updateUser({ avatar: filePath }, { id: users[0].id });
    if (!updateUser) {
      return res.json(result(msg="Upload avatar failed."));
    }
  }
  return res.json(result(code=200, msg="Upload avatar successfully."));
  // try {
  // } catch (error) {  
  //   res.status(500).send('Error uploading image', error);  
  // }  
});

module.exports = router;
