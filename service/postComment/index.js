const db = require("../../db");

const insertPostComment = async (postComment) => {
  await db.insert("post_comments", postComment);
}

const getPostComments = async (postId) => {
  let sql = "select pc.id, pc.content as content, pc.created_at, u.username as commenter from post_comments pc, users u where pc.comment_user_id = u.id and pc.post_id = ?";
  return db.query(sql, [postId]);
}

module.exports = {
  insertPostComment,
  getPostComments,
}