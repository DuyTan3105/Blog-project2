import { privateAxios } from "./helper";
import { myAxios } from "./helper";
import data from "bootstrap/js/src/dom/data";
import post from "../components/Post";
//create post function
export const createPost = (postData) => {
  //   console.log(postData);
  return privateAxios
    .post(
      `/posts/add/${postData.userId}/${postData.categoryId}`,
      postData
    )
    .then((response) => response.data);
};

//get all posts

export const loadAllPosts = (pageNumber, pageSize) => {
  return myAxios
    .get(
      `/posts/get-all-posts?pageNumber=${pageNumber}&pageSize=${pageSize}&sortBy=addedDate&sortDir=desc`
    )
    .then((response) => response.data);
};

//load single post of given id
export const loadPost = (postId) => {
  return myAxios.get("/posts/get/" + postId).then((reponse) => reponse.data);
};

export const loadCommentByPostId = (postId) => {
  return myAxios.get(`/comments/get/${postId}`).then((response) => response.data);
}

export const createComment = (comment, postId,userId,commentId) => {
  return privateAxios.post(`/comments/post/${postId}/${userId}?commentId=${commentId}`, comment).then(res=>res.data);
};

//upload post banner image

export const uploadPostImage = (image, postId) => {
  let formData = new FormData();
  formData.append("image", image);
  return privateAxios
    .post(`/posts/post/image/upload/${postId}`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    })
    .then((response) => response.data);
};

//get cateory wise posts
export function loadPostCategoryWise(categoryId) {
  return privateAxios
    .get(`/posts/category/${categoryId}/get`)
    .then((res) => res.data);
}

export function loadPostUserWise(userId) {
  return privateAxios.get(`/posts/user/${userId}/get`).then((res) => res.data);
}

//delete post
export function deletePostService(postId) {
  return privateAxios.delete(`/posts/delete/${postId}`).then((res) => res.data);
}

//update post
export function updatePost(post, postId) {
  console.log(post);
  return privateAxios.put(`/posts/update/${postId}`, post).then((resp) => resp.data);
}

export function getReplies(commentId) {
  return myAxios.get(`/comments/get-replies/${commentId}`).then(res => res.data);
}

export function searchPostS(key) {
  return myAxios.get(`/posts/search/${key}`).then(res => res.data);
}

export function deleteComment(commentId) {
  return privateAxios.delete(`/comments/delete/${commentId}`).then(data => data.data)
}

export function getAllInteractions(postId) {
    return myAxios.get(`/posts/interaction/${postId}`).then(res => res.data);
}

export const interact  =  async  (postId,userId,type) => {
  return await privateAxios.put(`/posts/interaction/${postId}/${userId}?type=${type}`);
}

export const checklikde = async (postId,userId) => {
  return await privateAxios.get(`/posts/checkliked/${postId}/${userId}`).then(data=>data.data)
}
