import { myAxios,privateAxios } from "./helper";

export const signUp = (user) => {
  return myAxios.post("/auth/register", user).then((response) => response.data);
};

export const loginUser = (loginDetail) => {
  return myAxios
    .post("/auth/login", loginDetail)
    .then((response) => response.data);
};

export const getUser = (userId) => {
  return myAxios.get(`/users/get/${userId}`).then((resp) => resp.data);
};

export const updateAvatar = async (userId, formData) => {
  try {
    const response = await privateAxios.put(`/users/update/avatar/${userId}`, formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }

    });
    console.log('Upload successful', response.data);
    return response.data;
    // Do something with response.data if needed
  } catch (error) {
    console.error('Error uploading file:', error);
    // Handle error
  }
};

export const forgotPass = (email) => {
  return privateAxios.post('/auth/forgotPass', email).then((response) => response.data);
}

export const changePasword= async (changPass) => {
  try {
    const data = await privateAxios.post("/auth/changePass",changPass);
    return data.data
  } catch (ex) {
    console.log(ex.message)
  }
}
