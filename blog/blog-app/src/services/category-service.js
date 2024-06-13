import {myAxios, privateAxios} from "./helper";

export const loadAllCategories = () => {
  return myAxios.get(`/category/get-all`).then((respone) => {
    return respone.data;
  });
};

export const searchKeyword = (keywords) => {
  return myAxios.get(`/posts/search/${keywords}`).then(res=> res.data)
}