const express = require('express');
const db = require('./db');
 
const app = express();
const port = 3000;
app.use(express.json());
 
app.get('/users', async (req, res) => {
  try {
    db.select('users').then(users => {
      console.log("users: ", users);
      return res.json(users);
    })
  } catch (error) {
    res.status(500).send('Server error');
  }
});
 
app.post('/users', async (req, res) => {
  const userData = { username: req.body.username, email: req.body.email, password: req.body.password };
  await db.insert('users', userData);
  return res.status(201).json('User created');
  // try {
  // } catch (error) {
  //   res.status(500).send('Server error');
  // }
});
 
app.listen(port, () => {
  console.log(`Server is running on port ${port}`);
});