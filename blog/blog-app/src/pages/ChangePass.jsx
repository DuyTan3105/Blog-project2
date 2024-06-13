import React, { useContext, useState } from 'react';
import { toast } from "react-toastify";
import userContext from "../context/userContext";
import {changePasword} from "../services/user-service";
import {doLogout} from "../auth";
import {useNavigate} from "react-router-dom";

const ChangePassword = () => {
    const navigate = useNavigate()
    const useContextData = useContext(userContext);
    const [confirm, setConfirm] = useState("");
    const [changePass, setChangePass] = useState({
        old: "",
        new: "",
        userId: useContextData.user.data.id
    });
    const [message, setMessage] = useState('');

    const handleChange = (e) => {
        const { name, value } = e.target;
        setChangePass({ ...changePass, [name]: value });
    };

    const handleSubmit = (e) => {
        e.preventDefault();
        if (changePass.old.trim() === "" || changePass.new === "" || confirm.trim() === "") {
            toast("Please enter all fields");
            return;
        }
        if (changePass.new !== confirm) {
            toast("Please confirm the password");
            return;
        }

        changePasword(changePass).then(data => toast(data.message)).catch(ex => console.log(ex.message))
        doLogout(()=> navigate("/login"));
    };

    return (
        <div className="max-w-md mx-auto my-8 p-6 bg-gray-100 rounded-md shadow-md">
            <h2 className="text-xl font-bold mb-4 text-red-500 text-center">Thay đổi mật khẩu</h2>
            <form onSubmit={handleSubmit}>
                <div className="mb-4">
                    <label htmlFor="oldPassword" className="block text-sm font-semibold mb-2">Mật khẩu cũ:</label>
                    <input
                        type="password"
                        id="oldPassword"
                        name="old"
                        value={changePass.old}
                        onChange={handleChange}
                        className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring focus:border-blue-400"
                        required
                    />
                </div>
                <div className="mb-4">
                    <label htmlFor="newPassword" className="block text-sm font-semibold mb-2">Mật khẩu mới:</label>
                    <input
                        type="password"
                        id="newPassword"
                        name="new"
                        value={changePass.new}
                        onChange={handleChange}
                        className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring focus:border-blue-400"
                        required
                    />
                </div>
                <div className="mb-4">
                    <label htmlFor="confirmNewPassword" className="block text-sm font-semibold mb-2">Xác nhận mật khẩu mới:</label>
                    <input
                        type="password"
                        id="confirmNewPassword"
                        name="confirm"
                        value={confirm}
                        onChange={(e) => setConfirm(e.target.value)}
                        className="w-full px-3 py-2 border rounded-md focus:outline-none focus:ring focus:border-blue-400"
                        required
                    />
                </div>
                <button type="submit" className="block w-1/2 mx-auto bg-blue-500 text-white py-2 rounded-md hover:bg-blue-600">Thay đổi mật khẩu</button>
            </form>
            {message && <p>{message}</p>}
        </div>
    );
};

export default ChangePassword;
