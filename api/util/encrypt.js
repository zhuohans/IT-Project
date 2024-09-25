const jwt = require('jsonwebtoken');
const crypto = require("crypto");
const { TOEKN_SECRET } = require("../util/constants");

/**
 * sign the specified data
 * @param {*} data 
 * @param {*} time 
 */
const sign = (data, time = 3600 * 24 * 1) => {
    const token = 'Bearer ' + jwt.sign(data, TOEKN_SECRET, { expiresIn: time });
    return token;
};

exports.sign = sign;
/**
 * encrypt info
 * @param {*} info 
 * @returns 
 */
exports.encript = (info) => {
    let md5 = crypto.createHash("md5");
    md5.update(info);
    return md5.digest("hex");
};