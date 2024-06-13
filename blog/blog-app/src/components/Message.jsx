import React from 'react';
import { Avatar, Input} from '@material-tailwind/react';

const Message = ({ text, displayName, createdAt, photoURL }) => {
    return (
        <div className="mb-10">
            <div className="flex items-center">
                <Avatar size='small' src={photoURL}>
                    {photoURL ? '' : displayName?.charAt(0)?.toUpperCase()}
                </Avatar>
                <Input className='author font-bold'>{displayName}</Input>
                <Input className='date ml-10 text-xs text-gray-400'>
                    {/* Replace the formatRelative function with your preferred date formatting */}
                    {createdAt}
                </Input>
            </div>
            <div>
                <Input className='content ml-30'>{text}</Input>
            </div>
        </div>
    );
}

export default Message;
