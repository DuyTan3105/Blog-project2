import React, { useState, useContext } from "react";
import { toast } from "react-toastify";

import Base from "../components/Base";
import { loginUser } from "../services/user-service";
import { doLogin } from "../auth";
import {Link, useNavigate} from "react-router-dom";
import userContext from "../context/userContext";
import {
  Card,
  Input,
  Checkbox,
  Button,
  Typography,
} from "@material-tailwind/react";

const Login = () => {
  const userContxtData = useContext(userContext);
  const navigate = useNavigate();

  const [loginDetail, setLoginDetail] = useState({
    username: "",
    password: "",
  });

  const handleChange = (event, field) => {
    let actualValue = event.target.value;
    setLoginDetail({
      ...loginDetail,
      [field]: actualValue,
    });
  };


  const handleFormSubmit = async (event) => {
    event.preventDefault();

    // Validation
    if (
      loginDetail.username.trim() === "" ||
      loginDetail.password.trim() === ""
    ) {
      toast.error("Username or Password is required!!");
      return;
    }

    try {
      // Submit the data to server to generate token
      const data = await loginUser(loginDetail);

      // Save the data to localStorage
      doLogin(data, () => {
        userContxtData.setUser({
          data: data.user,
          login: true,
        });
        navigate("/user/dashboard");
      });

      toast.success("Login Success");
    } catch (error) {
      if (error.response.status === 400 || error.response.status === 404) {
        toast.error(error.response.data.message);
      } else {
        toast.error("Something went wrong on server!!");
      }
    }
  };

  return (
      <Base>
      <section className="m-8 flex gap-4 justify-center ">
        <div className="w-full lg:w-3/5 mt-24">
          <div className="text-center">
            <Typography variant="h2" className="font-bold mb-4">Sign In</Typography>
            <Typography variant="paragraph" color="blue-gray" className="text-lg font-normal">Enter your email and password to Sign In.</Typography>
          </div>
          <form className="mt-8 mb-2 mx-auto w-80 max-w-screen-lg lg:w-1/2">
            <div className="mb-1 flex flex-col gap-6">
              <Typography variant="small" color="blue-gray" className="-mb-3 font-medium">
                Your email
              </Typography>
              <Input
                  size="lg"
                  placeholder="name@mail.com"
                  className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
                  labelProps={{
                    className: "before:content-none after:content-none",
                  }}
                  onChange={(e)=> handleChange(e,"username")}
              />
              <Typography variant="small" color="blue-gray" className="-mb-3 font-medium">
                Password
              </Typography>
              <Input
                  type="password"
                  size="lg"
                  placeholder="********"
                  className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
                  labelProps={{
                    className: "before:content-none after:content-none",
                  }}
                  onChange={(e)=> handleChange(e,"password")}
              />
            </div>

            <Button className="mt-6" fullWidth={true} color={"blue"} onClick={handleFormSubmit} >
              Sign In
            </Button>

            <div className="flex items-center justify-between gap-2 mt-6">

              <Typography variant="small" className="font-medium text-gray-900">
                <Link to={"/forgot-password"}>
                  Forgot Password
                </Link>
              </Typography>
            </div>

            <Typography variant="paragraph" className="text-center text-blue-gray-500 font-medium mt-4">
              Not registered?
              <Link to="/signup" className="text-gray-900 ml-1">Create account</Link>
            </Typography>
          </form>

        </div>


      </section>
      </Base>
  );
}


export default Login;
