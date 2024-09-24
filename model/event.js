const mongoose = require("./mongo");

const Event = mongoose.model("Event", {
  title: { type: String, required: true },
  location: { type: String },
  creator: { type: mongoose.Types.ObjectId, ref: "User" },
  start: {
    type: Date,
    required: true,
    get: (v) => moment(v).format("MMMM Do YYYY, h:mm a"),
  },
  end: {
    type: Date,
    required: true,
    get: (v) => moment(v).format("MMMM Do YYYY, h:mm a"),
  },
  createAt: {
    type: Date,
    default: Date,
    get: (v) => moment(v).format("MMMM Do YYYY, h:mm a"),
  },
});

module.exports = { Event };
