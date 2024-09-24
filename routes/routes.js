const express = require("express");
const router = express.Router();

/**
 * Summary of all routes
 * @returns 
 */
const route = () => {
    const userRouter = require("../controller/user");
    const categoryRouter = require("../controller/category");
    const eventRouter = require("../controller/event");
    const plantRouter = require("../controller/plant");
    const plantCollectionRouter = require("../controller/plantCollection");
    const postRouter = require("../controller/post");
    router.use("/user", userRouter);
    router.use("/categories", categoryRouter);
    router.use("/events", eventRouter);
    router.use("/plants", plantRouter);
    router.use("/plantCollections", plantCollectionRouter);
    router.use("/posts", postRouter);
    return router;
};

module.exports = route;
