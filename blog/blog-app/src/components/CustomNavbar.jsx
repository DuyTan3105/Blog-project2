import { useContext, useEffect, useState } from "react";
import { NavLink as ReactLink, useNavigate } from "react-router-dom";
import userContext from "../context/userContext";
import {isLoggedIn,getCurrentUserDetail,doLogout} from "../auth";
import { FaEdit,FaUserLock } from "react-icons/fa";
import { FaHome } from "react-icons/fa";
import { MdOutlineFeed } from "react-icons/md";
const CustomNavbar = () => {
    const userContextData = useContext(userContext);
    let navigate = useNavigate();
    const [isOpen, setIsOpen] = useState(false);
    const [login, setLogin] = useState(false);
    const [user, setUser] = useState(undefined);

    useEffect(() => {
        setLogin(isLoggedIn());
        setUser(getCurrentUserDetail());
    }, [login]);

    const logout = () => {
        doLogout(() => {
            //logged out
            setLogin(false);
            userContextData.setUser({
                data: null,
                login: false
            });
            navigate("/");
        });
    };

    return (
        <nav className="bg-gray-700 shadow-lg ">
            <div className="container mx-auto px-6 py-3">
                <div className="flex items-center h-full justify-content-between">
                    <div className="flex gap-[60px]">
                        <ReactLink to="/" className="text-white text-4xl font-semibold">
                            <FaHome/>
                        </ReactLink>
                        <ReactLink
                            to="/"
                            className="text-white text-4xl font-semibold"
                        >
                            <MdOutlineFeed/>
                        </ReactLink>

                        <ReactLink
                            to="/user/dashboard"
                            className="text-white text-4xl font-semibold"
                        >
                           <FaEdit/>
                        </ReactLink>


                        <ReactLink
                            to="/admin"
                            className="text-white text-4xl font-semibold"
                        >
                            <FaUserLock/>
                        </ReactLink>

                        <ReactLink
                            to="/user/chat"
                            className="text-white text-4xl font-semibold"
                        >
                            Discuss
                        </ReactLink>


                    </div>



                    <div className="ml-8 relative">
                        {login ? (
                            <div className="relative">
                                <button
                                    className="flex text-white focus:outline-none items-center"
                                    onClick={() => setIsOpen(!isOpen)}
                                >
                                    <img
                                        className="w-12 h-12 rounded-full mr-2"
                                        src={`/images/${user.avatarUrl}`} // Thay đổi thành đường dẫn ảnh đại diện thực tế
                                        alt="Avatar"
                                    />

                                </button>
                                <div
                                    className={` absolute right-0 mt-2 w-48 bg-white rounded-lg shadow-lg py-1 ${isOpen ? "block" : "hidden"}`}>
                                    <ReactLink
                                        to={`/user/profile-info/${user.id}`}
                                        className="block px-4 py-2 text-gray-800 hover:bg-gray-300"
                                    >
                                        Profile
                                    </ReactLink>
                                    <ReactLink
                                        to={"/change-password"}
                                        className="block px-4 py-2 text-gray-800 hover:bg-gray-300"
                                    >
                                        Change Password
                                    </ReactLink>
                                    <button
                                        onClick={logout}
                                        className="block px-4 py-2 text-gray-800 hover:bg-gray-300 w-full text-left"
                                    >
                                        Logout
                                    </button>
                                </div>
                            </div>
                        ) : (
                            <>
                                <ReactLink
                                    to="/login"
                                    className="text-white text-xl mt-4 mr-4"
                                >
                                Login
                                </ReactLink>
                                <ReactLink
                                    to="/signup"
                                    className="text-white text-xl mt-4"
                                >
                                    Signup
                                </ReactLink>
                            </>
                        )}
                    </div>
                </div>
            </div>

        </nav>
    );
};

export default CustomNavbar;
