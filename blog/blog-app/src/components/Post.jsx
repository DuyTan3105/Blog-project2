import React, {useContext, useEffect, useState} from "react";
import {Link, useNavigate} from "react-router-dom";
import moment from "moment";
import {
    IoCalendarOutline,

    IoChatbubbleOutline,
    IoShareSocialOutline,
    IoThumbsUp,
    IoThumbsUpOutline,
} from "react-icons/io5";
import {checklikde, getAllInteractions} from "../services/post-service";
import {interact} from "../services/post-service";
import userContext from "../context/userContext";
import { FacebookShareButton,FacebookIcon, TwitterShareButton,TwitterIcon, LinkedinShareButton,LinkedinIcon } from 'react-share';
function Post({ post, deletePost }) {
    const [showInteraction, setShowInteraction] = useState(false);
    const [liked, setLiked] = useState(false);
    const [shared, setShared] = useState(false);

    const [interactions,setInteractions] = useState({likes:0,comments:0,shares:0})
    const navigate = useNavigate();
    const contextData = useContext(userContext);
    useEffect(() => {

        getAllInteractions(post.postId).then(data => setInteractions({...data}))
        if(contextData.user.data) checklikde(post.postId,contextData.user.data.id).then(data => setLiked(data))

    }, []);

    const [showShareOptions, setShowShareOptions] = useState(false);

    // Toggle the display of the share options
    const toggleShareOptions = () => {
        setShowShareOptions(!showShareOptions);
    };



    const handleLikeorShare = async (postId,userId,type) => {
            try {
                const data = await interact(postId, userId, type);
                if(type== "LIKE") {
                    setInteractions(prevInteractions => ({
                        ...prevInteractions,
                        likes: liked ? prevInteractions.likes - 1 : prevInteractions.likes + 1
                    }));
                        setLiked(!liked)
                }
            } catch (ex) {
                console.log(ex.message);
            }
        }

        const handleComment = () => {
            navigate(`/posts/${post.postId}#comments`);
        }



    const stripHtmlTags = (html) => {
        const doc = new DOMParser().parseFromString(html, "text/html");
        return doc.body.textContent || "";
    };

    const limitText = (text, limit) => {
        if (text.length <= limit) return text;
        return text.substring(0, limit) + "...";
    };

    const plainTextContent = stripHtmlTags(post.content);
    const limitedContent = limitText(plainTextContent, 1000);

    return (
        <div className="bg-white shadow-md rounded-md p-4 mx-auto my-2 border-2 border-gray-500">
            <div className="ml-2">
                <Link className="text-3xl" to={`/posts/${post.postId}`}>
                    {post.title}
                </Link>
                <div className="flex items-center mt-3">
                    <div className="text-xl mr-3 flex">
                        <IoCalendarOutline className="mt-1 mr-2" />
                        {moment(post.addedDate).format("DD/MM/YYYY")}
                    </div>
                    <div className="italic text-xl text-green-500">
                        {"By " + post.user.name}
                    </div>
                </div>
            </div>

            <hr className="my-4" />

            <div className="inline-block w-full">
                <img
                    src={`/images/${post.imageUrl}`}
                    className="mr-4 float-left h-[150px] w-[250px]"
                    alt="Post"
                />
                <div className="leading-relaxed">{limitedContent}</div>
            </div>

            <div className="text-right mt-4">
                <Link className="text-blue-500" to={`/posts/${post.postId}`}>
                    Read More
                </Link>
            </div>

            <hr className="mt-10" />

            <div className="flex justify-between mt-4">
                <div className="flex items-center">
                    <button onClick={(e) => handleLikeorShare(post.postId,contextData.user.data.id,"LIKE")} className="mr-4 flex items-center">
                        {liked ? (
                            <IoThumbsUp className="text-blue-500 text-2xl" />
                        ) : (
                            <IoThumbsUpOutline className="text-blue-500 text-2xl" />
                        )}
                        <span className="ml-1">{interactions.likes}</span>
                    </button>
                    <button
                        onClick={handleComment}
                        className="mr-4 flex items-center"
                    >
                        <IoChatbubbleOutline className="text-blue-500 text-2xl" />
                        <span className="ml-1">{interactions.comments}</span>
                    </button>
                    <button onClick={toggleShareOptions} className="flex items-center">
                        <IoShareSocialOutline className="text-blue-500 text-2xl"/>
                        <span className="ml-1">{interactions.shares}</span>
                        {shared &&
                            <span className="ml-1">Shared</span>

                        }
                    </button>


                </div>
                <p className="font-bold italic text-[20px] text-red-500">
                    <i></i>&nbsp;&nbsp;{post.category.categoryTitle}
                </p>
            </div>
            {showShareOptions && (
                <span className="ml-16 bg-gray-300 inline-block pt-1">
                    <FacebookShareButton url={`http://localhost:3000/posts/${post.postId}`}>
                        <button className="hover:bg-blue-700 text-white px-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 focus:ring-offset-gray-100 ">
                            <FacebookIcon className="rounded-full size-12"/>
                        </button>
                    </FacebookShareButton>

                    <TwitterShareButton url={`http://localhost:3000//posts/${post.postId}`}>
                        <button className="hover:bg-blue-500 text-white  px-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 focus:ring-offset-gray-100">
                            <TwitterIcon className="rounded-full size-12"/>
                        </button>
                    </TwitterShareButton>

                    <LinkedinShareButton url={`http://localhost:3000//posts/${post.postId}`}>
                        <button className="hover:bg-blue-800 text-white px-2 rounded focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 focus:ring-offset-gray-100">
                            <LinkedinIcon className="rounded-full size-12"/>
                        </button>
                    </LinkedinShareButton>
                </span>
            )}
        </div>
    );
}


export default Post;
