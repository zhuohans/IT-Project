const db = require("../../db");

const insertPost = async (post, imagePaths) => {
  try {  
    const [postResult] = await db.db.execute('INSERT INTO posts (title, content, creator_id) VALUES (?, ?, ?)', [post.title, post.content, post.creator_id]);  
    // console.log("postResult: ", postResult.insertId);
    const postId = postResult.insertId; // get the returing id
  
    // insert post image path 
    for (const imagePath of imagePaths) {  
      await db.db.execute('INSERT INTO post_images (post_id, src) VALUES (?, ?)', [postId, imagePath]);  
    }  
  
    return { postId, success: true };  
  } catch (error) {  
    console.error('Error creating post and images:', error);  
    throw error;
  }
}

const getPosts = async () => {
  let sql = "select p.id, p.title, p.content, p.created_at, pi.src as src, u.username as creator, u.avatar as creator_avatar from posts p left join users u on p.creator_id = u.id left join post_images pi on p.id = pi.post_id order by created_at desc";
  const posts = await db.query(sql, []);
  let postMap = {};
  let postDetailList = [];
  if (posts.length > 0) {
    // console.log('posts: ', posts);
    for (const post of posts) {
      let p = postMap[post.id];
      if (p) {
        p.push(post);
      } else {
        postMap[post.id] = [post];
      }
    }
    for (const key of Object.keys(postMap)) {
      const postImages = postMap[key];
      const post = postImages[0];
      let p = {
        id: post.id,
        title: post.title,
        content: post.content,
        created_at: post.created_at,
        creator: post.creator,
        creator_avatar: post.creator_avatar,
        images: []
      }
      if (postImages && postImages.length > 0) {
        let images = [];
        for (const postImage of postImages) {
          images.push(postImage.src);
        }
        p.images = images;
      }
      postDetailList.push(p);
    }
    postDetailList.sort((a, b) => -1 * (a.created_at - b.created_at))
    console.log(postDetailList);
    return postDetailList;
  }
}

const getPostById = async (postId) => {
  let sql = "select p.id, p.title, p.content, p.created_at, pi.src as src, u.username as creator from posts p left join users u on p.creator_id = u.id left join post_images pi on p.id = pi.post_id where p.id = ?";
  const posts = await db.query(sql, [postId]);
  let postMap = {};
  let postDetailList = [];
  if (posts.length > 0) {
    for (const post of posts) {
      let p = postMap[post.id];
      if (p) {
        p.push(post);
      } else {
        postMap[post.id] = [post];
      }
    }
    for (const key of Object.keys(postMap)) {
      const postImages = postMap[key];
      if (postImages && postImages.length > 0) {
        let images = [];
        const post = postImages[0];
        for (const postImage of postImages) {
          images.push(postImage.src);
        }
        let p = {
          id: post.id,
          title: post.title,
          content: post.content,
          created_at: post.created_at,
          creator: post.creator,
          images
        }
        postDetailList.push(p);
      }
    }

    return postDetailList[0];
  }
}

module.exports = {
  insertPost,
  getPosts,
  getPostById,
}