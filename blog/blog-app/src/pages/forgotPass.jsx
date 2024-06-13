import React, { useState } from 'react';
import Base from './../components/Base';
import { toast } from 'react-toastify';
import { forgotPass } from '../services/user-service';
import {json, useNavigate} from 'react-router-dom';

const ForgotPass = () => {
    const [mail, setMail] = useState({ gmail: '' });
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        if (mail.gmail === '') {
            toast.warning('Please enter your email');
        } else {
            console.log(mail)
          forgotPass(mail).then(data => {
              console.log(data);
              toast(data.message)
              setTimeout(() => navigate('/login'),2000)
          })
        }
    };

    return (
        <Base>
            <div className="mt-10 text-center flex mx-auto w-[500px] bg-white rounded-lg shadow-md p-4">
                <form className="w-full" onSubmit={handleSubmit}>
                    <h3 className="text-2xl font-semibold mb-4">Forgot Password</h3>
                    <div className="flex items-center mb-4">
                        <label htmlFor="email" className="mr-2 text-lg font-bold w-36 text-red-500">Email address</label>
                        <input
                            type="email"
                            id="email"
                            aria-describedby="emailHelp"
                            placeholder="Enter your email"
                            value={mail.gmail}
                            onChange={(e) => setMail({ gmail: e.target.value })}
                            className="w-full py-2 px-4 border border-gray-400 rounded-md focus:outline-none focus:border-blue-500"
                        />
                    </div>
                    <button type="submit" className="btn btn-primary">Submit</button>
                </form>
            </div>
        </Base>
    );
};

export default ForgotPass;
