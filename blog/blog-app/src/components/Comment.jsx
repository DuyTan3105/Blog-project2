import React, { useContext, useEffect, useState } from 'react';
import { createComment, deleteComment, getReplies } from "../services/post-service";
import userContext from "../context/userContext";
import moment from "moment";
import { HiDotsVertical } from "react-icons/hi";
import { toast } from "react-toastify";


const Comment = ({ comment, delcomment }) => {
    const [subcomments, setSubComments] = useState([]);
    const userContextData = useContext(userContext);
    const [showReplies, setShowReplies] = useState(false);
    const [showCommentForm, setShowCommentForm] = useState(false);

    const [showOptionsMenu, setShowOptionsMenu] = useState(false); // State để xác định việc hiển thị menu tùy chọn

    useEffect(() => {
            getReplies(comment.id)
                .then(data => {console.log(data); setSubComments(data)})
                .catch(err => console.log(err.message));

    }, [comment.id]);
    const handleShowCommentForm = () => {
        setShowCommentForm(!showCommentForm);
    };

    const handlegetReplies = () => {
        setShowReplies(!showReplies);
    };


    const handleMoreOptionsClick = () => {
        setShowOptionsMenu(!showOptionsMenu);
        console.log(showOptionsMenu)
    };

    const handleEditComment = async (id) => {
        // Logic để chỉnh sửa comment

    };

    const handleReportComment = () => {
        // Logic để report comment
        console.log("Report comment");
    };

    const handeDeleteComment = (id) => {
        deleteComment(id).then(
            toast.info("Deleted comment Successfully"));
    }

    const handleReply = async (reply) => {
        try {
            const data = await createComment(reply,comment.postId,userContextData.user.data.id,comment.id );
            setSubComments([...subcomments,data])
        } catch (ex) {
            console.log(ex.message);
        }
    }

    return (
        <li className="flex items-start py-1 border-t border-gray-200">
            <div className="mr-4">
                <img src={`/images/${comment.user.avatarUrl}`} alt="Avatar" className="w-12 h-12 rounded-full" />
            </div>
            <div className="w-full">
                <div className="bg-gray-100 rounded-lg p-3 relative">
                    <div className="block float-end size-auto">
                        {/* Nút ba chấm */}

                        <button className="text-sm font-medium text-blue-500 float-end flex flex-row" onClick={e => {handleMoreOptionsClick();}}>
                            <HiDotsVertical size="18px" className="mt-2 mr-2"/>
                            {showOptionsMenu && (
                                <div className="border border-gray-300 shadow-md rounded-md p-2">
                                    {userContextData.user.data.id == comment.user.id && (
                                        <>
                                            <button className="block w-full text-left mb-2"
                                                    onClick={(e) => delcomment(comment.id)}>Delete
                                            </button>
                                            <button className="block w-full text-left mb-2"
                                                    onClick={handleEditComment}>Edit
                                            </button>
                                        </>
                                    )}

                                    <button className="block w-full text-left" onClick={handleReportComment}>Report</button>
                                </div>
                            )}
                        </button>




                    </div>

                    <div className="flex items-center mb-2">
                        <span className="font-semibold text-[17px]">{comment.user.name}</span>
                        <span className="ml-2 text-xl italic text-[14px]">{moment(comment.creationDate).format("DD/MM/YYYY")}</span>
                    </div>
                    <p className="text-[16px]">{comment.content}</p>
                    {userContextData && (
                        <button onClick={handleShowCommentForm} className="mr-3 text-sm font-medium text-blue-500">
                            Reply
                        </button>
                    )}
                    <button onClick={handlegetReplies} className="text-sm font-medium text-blue-500">
                        {showReplies ? 'Hide Replies' : 'Show Replies ('  + subcomments.length+ ')'}
                    </button>

                    <hr/>
                    {showCommentForm && (
                        <CommentForm onSubmit={handleReply} showCommentForm={handleShowCommentForm} />
                    )}
                    {showReplies && (<Reply postId={comment.postId} parentId={comment.id} show={showReplies} subcomments={subcomments} setSubComments={setSubComments} />)}

                </div>


            </div>

        </li>

    );
};

export {Comment};


const Reply = ({ parentId,postId, delcomment,subcomments, setSubComments }) => {

    const userContextData = useContext(userContext);


    const [showCommentForm, setShowCommentForm] = useState(false);
    const handleShowCommentForm = () => {
        setShowCommentForm(!showCommentForm);
    };

    const handleReply = async (reply) => {
        try {
            const data = await createComment(reply,postId,userContextData.user.data.id,parentId); // Chỉnh sửa dữ liệu truyền vào theo nhu cầu
            setSubComments([...subcomments, data]);
        } catch (ex) {
            console.log(ex.message);
        }
    };

    return (
        <ul className="mt-2">
            {subcomments.map(reply => (
                <li key={reply.id} className="bg-gray-100 rounded-lg mt-3">
                    <div className="block float-end">
                        {/* Nút ba chấm */}
                        <button className="text-sm font-medium text-blue-500 "
                                >
                            <HiDotsVertical size="18px" className="mt-2 mr-2"/>
                        </button>

                        {/* Menu tùy chọn */}

                    </div>
                    <div className="container">

                        <div className="items-center flex ml-2 ">
                            <img src={`/images/${reply.user.avatarUrl}`} alt="Avatar"
                                 className="w-12 h-12 rounded-full mt-1"/>
                            <span className="ml-2 font-semibold text-[17px]">{reply.user.name}</span>
                            <span
                                className="ml-2 text-xl italic text-[14px]">{moment(reply.creationDate).format("DD/MM/YYYY")}</span>
                        </div>
                        <div>
                            <div className="text-[16px] w-full
                                                                            ml-[63px]">{reply.content}</div>
                            <button onClick={handleShowCommentForm}
                                    className="ml-2 mt-2 text-sm font-medium text-blue-500">
                                Reply
                            </button>
                            {userContextData.user.data.id === reply.user.id && (
                                <button className="ml-3 mt-2 text-sm font-medium text-blue-500"
                                        onClick={() => {
                                            delcomment(reply.id);
                                            setSubComments(subcomments.filter(comment => comment.id !== reply.id))
                                        }}>Delete</button>
                            )}
                            <hr/>
                        </div>
                    </div>
                </li>
            ))}
            {showCommentForm && (

                <CommentForm onSubmit={handleReply} showCommentForm={handleShowCommentForm}/>
            )}


        </ul>
    );
};

export {Reply};

const CommentList = ({id, comments, setComments}) => {
    const delcomment = async (commentId) => {
        try {
            const data = await deleteComment(commentId);
            setComments(comments.filter(cmt => cmt.id !== commentId))
            toast.info(data.message)
        } catch (ex) {
            console.log(ex.message)
        }
    }
    return (
        <ol id={id} className="space-y-4">
            {comments.map(comment => (
                <Comment key={comment.id} comment={comment} delcomment={delcomment}/>
            ))}
        </ol>
    );
};

export {CommentList};

const CommentForm = ({onSubmit, showCommentForm}) => {
    const [comment, setComment] = useState({content: ""});

    const handleSubmit = (e) => {
        e.preventDefault();
        onSubmit(comment);
        setComment({content: ""});
    };

    const handleChange = (e) => {
        setComment({content: e.target.value});
    };

    return (
        <form onSubmit={handleSubmit} className="comment-form mt-2">
            <textarea
                className="border border-gray-300 rounded-md p-2 mb-2 w-full"
                placeholder="Write your reply..."
                value={comment.content}
                onChange={handleChange}
            ></textarea>
            <button type="submit" className="bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600">
                Post Reply
            </button>
            {/* Hiển thị nút hủy nếu được chuyển vào props */}
            {showCommentForm && (
                <button type="button" onClick={showCommentForm}
                        className="bg-red-500 ml-5 text-white px-4 py-2 rounded-md hover:bg-red-600">
                    Cancel
                </button>
            )}
        </form>
    );
};


export {CommentForm};
