import React, { useEffect, useState } from 'react';
import { Button, Card, CardBody, CardFooter, Container, Table } from 'reactstrap';
import { getCurrentUserDetail } from '../auth';
import {updateAvatar} from "../services/user-service";
const ViewUserProfile = ({ user,setUser, updateProfileClick }) => {
    const [currentUser, setCurrentUser] = useState(null);
    const [formData,setFormData] = useState(new FormData())
    useEffect(() => {
        setCurrentUser(getCurrentUserDetail());
    }, []);

    const handleAvatarClick = () => {
        document.getElementById('avatarInput').click();
    };

    const handleAvatarChange = (event) => {
        const file = event.target.files[0];
        setFormData(new FormData())
        if (file) {
            formData.append('file', file);
            setFormData(formData);}
    };

        const uploadAvatar = async (e) => {
            try {
                const data = await updateAvatar(user.id,formData);
                console.log(data)
                setUser({...user,avatarUrl:data});
                const user = JSON.parse(localStorage.getItem("data")).user;
                user.avatarUrl=data;
                const updatedData = JSON.stringify(user);
                localStorage.setItem("data", updatedData);
            } catch (ex) {
                console.log(ex.message)
            }
        }


    return (
        <Card className='mt-2 border-0 rounded-0 shadow-sm'>
            <CardBody>
                <h3 className='text-uppercase text-center'>User Information</h3>
                <Container className='text-center mt-2'>
                    <div className="w-full flex justify-content-center">
                        <img
                            src={user.avatarUrl ? `/images/${user.avatarUrl}` : "/images/Avatar/dfavatar.png"}
                            alt="Avatar"
                            className="rounded-full w-36 h-36 "
                            onClick={handleAvatarClick}
                        />
                        <input
                        type="file"
                        id="avatarInput"
                        style={{display: 'none'}}
                        accept="image/*"
                        onChange={handleAvatarChange}
                    />

                    </div>

                    <button
                        type="button"
                        onClick={uploadAvatar}
                        className="mt-5 bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded-full"
                    >
                        Upload Avatar
                    </button>
                </Container>
                <Table responsive striped hover bordered className='text-center mt-5'>
                    <tbody>
                    <tr>
                        <td>LCWDBlLOGS avatar</td>
                        <td>LCWD{user.avatarUrl}</td>
                    </tr>
                    <tr>
                        <td>LCWDBlLOGS ID</td>
                        <td>LCWD{user.id}</td>
                    </tr>
                    <tr>
                        <td>User Name</td>
                        <td>{user.name}</td>
                    </tr>
                    <tr>
                        <td>User Email</td>
                        <td>{user.email}</td>
                    </tr>
                    <tr>
                        <td>About</td>
                        <td>{user.about}</td>
                    </tr>

                    </tbody>
                </Table>
                {currentUser ? (currentUser.id === user.id) ? (
                    <CardFooter className='text-center'>
                        <Button onClick={updateProfileClick} color='warning'>Update Profile</Button>
                    </CardFooter>
                ) : '' : ''}
            </CardBody>
        </Card>
    );
};

export default ViewUserProfile;
