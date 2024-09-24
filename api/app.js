const createError = require('http-errors');
const express = require('express');
const path = require('path');
const cookieParser = require('cookie-parser');
const logger = require('morgan');
const expressJwt = require("express-jwt");
const cors = require("cors");
const router = require("./routes/routes")();
const app = express();

const { TOEKN_SECRET } = require("./util/constants");

// view engine setup
// app.set('views', path.join(__dirname, 'views'));
// app.set('view engine', 'jade');

app.use(cors());

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({
    extended: false
}));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.all('*', function (req, res, next) {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', 'Content-Type, Content-Length, Authorization, Accept, X-Requested-With , yourHeaderFeild');
    res.header('Access-Control-Allow-Methods', 'PUT, POST, GET, DELETE, OPTIONS');

    next();
});

app.use(expressJwt({
    credentialsRequired: true,
    secret: TOEKN_SECRET,
    algorithms: ['HS256']
}).unless({
    path: [
      "/api/user/login", 
      "/api/user/register", 
      "/api/plants/list",
      "/api/posts/list",
      "/api/categories/list",
      new RegExp('^/api/plants/\\d+$'),
      new RegExp('^/api/plants/listByCategory/\\d+$'),
      new RegExp('^/api/posts/\\d+$'),
      new RegExp('^/api/posts/comments/\\d+$'),
    ] // Add routes that do not require token authentication
}));




app.use('/api', router);

// catch 404 and forward to error handler
app.use(function (req, res, next) {
    next(createError(404));
});

// error handler
app.use(function (err, req, res, next) {
    // set locals, only providing error in development
    res.locals.message = err.message;
    res.locals.error = req.app.get('env') === 'development' ? err : {};

    // render the error page
    res.status(err.status || 500);
    res.json({
        code: err.status,
        msg: err.message
    });
});

module.exports = app;