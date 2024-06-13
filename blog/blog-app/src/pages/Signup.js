import { useState } from "react";
import { signUp } from "../services/user-service";
import { toast } from "react-toastify";
import { FaUserPlus } from "react-icons/fa";
import Base from "../components/Base";
import { useNavigate,Link } from "react-router-dom";
import {
  Card,
  Input,
  Checkbox,
  Button,
  Typography,
} from "@material-tailwind/react";
const Signup = () => {
  const navigate = useNavigate();
  const [data, setData] = useState({
    email: "",
    name: "",
    password: "",
    about: "",
  });

  const [error, setError] = useState({
    errors: {},
    isError: false,
  });

  const handleChange = (event, property) => {
    setData({ ...data, [property]: event.target.value });
  };


  const submitForm = async (event) => {
    event.preventDefault();

    try {
      const resp = await signUp(data);
      console.log(resp);
      toast.success("User is registered successfully !! user id " + resp.id);
      navigate("/login")
   
    } catch (error) {
      console.log(error);
      setError({
        errors: error,
        isError: true,
      });
    }
  };

  return (
      <Base>
        <section className="m-8 flex justify-center">

          <div className="w-full lg:w-3/5 flex flex-col items-center justify-center">
            <div className="text-center">
              <Typography variant="h2" className="font-bold mb-4">Join Us Today</Typography>
              <Typography variant="paragraph" color="blue-gray" className="text-lg font-normal">Enter your email and
                password to register.</Typography>
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
                    onChange={(e)=> handleChange(e,"email")}
                />

                <Typography variant="small" color="blue-gray" className="-mb-3 font-medium">
                  Your password
                </Typography>
                <Input
                    size="lg"
                    type={"password"}
                    placeholder="******"
                    className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
                    labelProps={{
                      className: "before:content-none after:content-none",
                    }}
                    onChange={(e)=> handleChange(e,"password")}
                />

                <Typography variant="small" color="blue-gray" className="-mb-3 font-medium">
                  Your name
                </Typography>
                <Input
                    size="lg"
                    placeholder={"ABC"}
                    className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
                    labelProps={{
                      className: "before:content-none after:content-none",
                    }}
                    onChange={(e)=> handleChange(e,"name")}
                />

                <Typography variant="small" color="blue-gray" className="-mb-3 font-medium">
                  Your name
                </Typography>
                <Input
                    size="lg"
                    type={"date"}
                    placeholder={"ABC"}
                    className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
                    labelProps={{
                      className: "before:content-none after:content-none",
                    }}
                    onChange={(e)=> handleChange(e,"date")}
                />

                <Typography variant="small" color="blue-gray" className="-mb-3 font-medium">
                    About
                </Typography>
                <Input
                    size="lg"
                    type={"text"}
                    placeholder="????"
                    className=" !border-t-blue-gray-200 focus:!border-t-gray-900"
                    labelProps={{
                      className: "before:content-none after:content-none",

                    }}
                    onChange={(e)=> handleChange(e,"about")}
                />
              </div>

              <Button className="mt-6" fullWidth color={"blue"} onClick={submitForm}>
                Register Now
              </Button>


              <Typography variant="paragraph" className="text-center text-blue-gray-500 font-medium mt-4">
                Already have an account?
                <Link to="/login" className="text-gray-900 ml-1">Sign in</Link>
              </Typography>
            </form>

          </div>
        </section>
      </Base>
  );
};

export default Signup;
