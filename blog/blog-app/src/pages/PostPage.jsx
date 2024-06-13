import { useState, useEffect,useContext } from "react";
import { Link, useParams } from "react-router-dom";
import { Button, Card, CardBody, CardText, Col, Container, Input, Row } from "reactstrap";
import Base from "../components/Base";
import { createComment, loadPost } from "../services/post-service";
import { toast } from 'react-toastify';
import { BASE_URL } from "../services/helper";
import { isLoggedIn } from "../auth";
import {CommentList,Comment,CommentForm} from './../components/Comment';
import {loadCommentByPostId} from './../services/post-service';
import { signUp } from './../services/user-service';
import userContext from "../context/userContext";
import {useLocation} from "react-router-dom";
const PostPage = () => {
    const useContextData = useContext(userContext);
    const { postId } = useParams();
    const location = useLocation();
    const [post, setPost] = useState(null);
    const [comment, setComment] = useState({
        content: ''
    });
    const [commentList,setCommentList] = useState([]);

    useEffect(() => {
        const fetchPost = async () => {
            try {
                const data = await loadPost(postId);
                setPost(data);
            } catch (error) {
                console.log(error);
                toast.error("Error in loading post");
            }
        };

        const fetchComments = async () => {
            try {
                const data = await loadCommentByPostId(postId);
                setCommentList(data);
            } catch (error) {
                console.log(error);
                toast.error("Error in loading comments");
            }
        };
        const fetchData = async () => {
            await fetchPost();
            await fetchComments();

            // Sau khi đã fetch dữ liệu, kiểm tra location.hash và cuộn đến phần tử nếu cần
            if (location.hash) {
                const element = document.getElementById(location.hash.substring(1)); // Bỏ dấu "#" trong location.hash
                if (element) {
                    element.scrollIntoView({ behavior: 'smooth' });
                }
            }
        };

        fetchData();
        console.log(location.hash)
        console.log("inra ")
        // Scroll to comment section if hash is present in URL
        if (location.hash) {
            const element = document.getElementById(location.hash);

            if (element) {
                element.scrollIntoView({ behavior: 'smooth' });
            }
        }
    }, [postId, location.hash]);



    const printDate = (numbers) => {
        return new Date(numbers).toLocaleDateString();
    };

    const submitPost = async (comment) => {
        if (!isLoggedIn()) {
            toast.error("Need to login first !!");
            return;
        }

        if (comment.content.trim() === '') {
            return;
        }

        try {
        console.log("inra:" +comment.content)
            const data = await createComment(comment, post.postId,useContextData.user.data.id,0);
            console.log(data.data);
            toast.success("comment added ..");
            setCommentList([...commentList, data]);
            setComment({
                content: ''
            });
        } catch (error) {
            console.log(error);
        }
    };

    return (
        <Base>
            <Container className="mt-4">
                <Link to="/">Home</Link> / {post && (<Link to="">{post.title}</Link>)}
                <Row>
                    <Col md={{ size: 12 }}>
                        <Card className="mt-3 ps-2 border-0 shadow-sm">
                            {post && (
                                <CardBody>
                                    <CardText>
                                        Posted By <b>{post.user.name}</b> on <b>{printDate(post.addedDate)}</b>
                                    </CardText>
                                    <CardText>
                                        <span className="text-muted">{post.category.categoryTitle}</span>
                                    </CardText>
                                    <div className="divder" style={{ width: '100%', height: '1px', background: '#e2e2e2' }}></div>
                                    <CardText className="mt-3">
                                        <h1>{post.title}</h1>
                                    </CardText>
                                    <CardText className="mt-5" dangerouslySetInnerHTML={{ __html: post.content }}></CardText>
                                </CardBody>
                            )}
                        </Card>
                    </Col>
                </Row>
                <Row className="my-4">
                    <Col md={{ size: 9, offset: 1 }}>
                        <h3 id="comments">Comments</h3>
                        <CommentList comments={commentList} setComments={setCommentList} />
                        <CommentForm onSubmit={(comment) => submitPost(comment)}/>
                    </Col>
                </Row>
            </Container>
        </Base>
    );
};

export default PostPage;
