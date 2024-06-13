// import React, { useContext, useEffect, useRef, useState } from 'react';
// import { UserAddIcon } from '@heroicons/react/outline';
// import { Alert, Avatar, Button, Form, Input, Tooltip } from '@heroicons/react/solid';
// import Message from './Message';
// // import { AppContext } from '../../Context/AppProvider';
// import { AuthContext } from '../../Context/AuthProvider';
// import useFirestore from '../../hooks/useFirestore';
//
// const ChatWindow = () => {
//     const { selectedRoom, members, setIsInviteMemberVisible } = useContext(AppContext);
//     const { user: { uid, photoURL, displayName } } = useContext(AuthContext);
//     const [inputValue, setInputValue] = useState('');
//     const [form] = Form.useForm();
//     const inputRef = useRef(null);
//     const messageListRef = useRef(null);
//
//     const handleInputChange = (e) => {
//         setInputValue(e.target.value);
//     };
//
//     const handleOnSubmit = () => {
//         // Add your logic to handle form submission
//     };
//
//     const condition = React.useMemo(
//         () => ({
//             fieldName: 'roomId',
//             operator: '==',
//             compareValue: selectedRoom.id,
//         }),
//         [selectedRoom.id]
//     );
//
//     const messages = useFirestore('messages', condition);
//
//     useEffect(() => {
//         // Scroll to bottom after message changed
//         if (messageListRef?.current) {
//             messageListRef.current.scrollTop = messageListRef.current.scrollHeight + 50;
//         }
//     }, [messages]);
//
//     return (
//         <div className="h-screen">
//             {selectedRoom.id ? (
//                 <>
//                     <div className="flex justify-between items-center h-14 px-4 border-b border-gray-300">
//                         <div className="flex flex-col justify-center">
//                             <p className="font-bold">{selectedRoom.name}</p>
//                             <span className="text-sm">{selectedRoom.description}</span>
//                         </div>
//                         <div className="flex items-center space-x-2">
//                             <Button
//                                 icon={<UserAddIcon className="h-5 w-5" />}
//                                 onClick={() => setIsInviteMemberVisible(true)}
//                             >
//                                 Mời
//                             </Button>
//                             <div className="flex space-x-1">
//                                 {members.map((member) => (
//                                     <Tooltip content={member.displayName} key={member.id}>
//                                         <Avatar src={member.photoURL}>
//                                             {member.photoURL ? '' : member.displayName?.charAt(0)?.toUpperCase()}
//                                         </Avatar>
//                                     </Tooltip>
//                                 ))}
//                             </div>
//                         </div>
//                     </div>
//                     <div className="flex flex-col-reverse h-full">
//                         <div className="overflow-auto">
//                             {messages.map((mes, index) => (
//                                 <Message
//                                     key={index}
//                                     text={mes.text}
//                                     photoURL={mes.photoURL}
//                                     displayName={mes.displayName}
//                                     createdAt={mes.createdAt}
//                                 />
//                             ))}
//                         </div>
//                         <div className="border-t border-gray-300">
//                             <Form className="flex items-center justify-between p-2 border border-gray-300 rounded-md">
//                                 <Input
//                                     ref={inputRef}
//                                     onChange={handleInputChange}
//                                     value={inputValue}
//                                     placeholder="Nhập tin nhắn..."
//                                     autoComplete="off"
//                                     className="flex-1 focus:outline-none"
//                                 />
//                                 <Button type="primary" onClick={handleOnSubmit}>
//                                     Gửi
//                                 </Button>
//                             </Form>
//                         </div>
//                     </div>
//                 </>
//             ) : (
//                 <Alert
//                     message="Hãy chọn phòng"
//                     type="info"
//                     showIcon
//                     className="m-5"
//                     closable
//                 />
//             )}
//         </div>
//     );
// }
//
// export default ChatWindow;
