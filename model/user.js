const mongoose = require("./mongo");

const User = mongoose.model("User", {
    username: {
      type: String,
      required: true,
      unique: true
    },
    email: {
      type: String,
      required: true
    },
    age: {
      type: Number,
    },
    gender: {
      type: String,
    },
    phoneNumber: {
      type: String
    },
    address: {
      type: String
    },
    password: {
      type: String,
      required: true,
      minlength: [6, "Password must be at least 6 characters long."]
    },
    createAt: {
      type: Date,
      default: Date,
      get: (v) => moment(v).format("MMMM Do YYYY, h:mm a"),
    }
});

module.exports = {
    User
};