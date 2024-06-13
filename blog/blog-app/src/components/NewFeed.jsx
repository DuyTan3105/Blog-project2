import React from 'react'
import { useState } from 'react'
import { useEffect } from 'react'
import {loadAllPosts, searchPostS} from '../services/post-service'
import {Row, Col, Pagination, PaginationItem, PaginationLink, Container, Input, Button} from 'reactstrap'
import Post from './Post'
import { toast } from 'react-toastify'
import InfiniteScroll from 'react-infinite-scroll-component'
import { deletePostService } from '../services/post-service'
function NewFeed() {


    const [postContent, setPostContent] = useState({
        content: [],
        totalPages: '',
        totalElements: '',
        pageSize: '',
        lastPage: false,
        pageNumber: ''

    })

    const [currentPage, setCurrentPage] = useState(0)
    const [keyword, setKeyword] = useState('');

    useEffect(() => {
        console.log("loading posts")
        console.log(currentPage)
        changePage(currentPage)

    }, [currentPage])

    const searchPostsByKeyword = () => {
        if (keyword.trim() === '') {
            toast.error("Please enter a keyword.");
            return;
        }

        searchPostS(keyword)
            .then(data => {
                console.log(data)
                setPostContent({
                    content: [...data.content],
                    totalPages: data.totalPages,
                    totalElements: data.totalElements,
                    pageSize: data.pageSize,
                    lastPage: data.lastPage,
                    pageNumber: data.pageNumber
                });
            })
            .catch(error => {
                console.log(error);
                toast.error("Error in searching posts.");
            });
    };

    const changePage = (pageNumber = 0, pageSize = 5) => {
        if (pageNumber > postContent.pageNumber && postContent.lastPage) {
            return
        }
        if (pageNumber < postContent.pageNumber && postContent.pageNumber == 0) {
            return
        }
        loadAllPosts(pageNumber, pageSize).then(data => {
            setPostContent({
                content: [...postContent.content, ...data.content],
                totalPages: data.totalPages,
                totalElements: data.totalElements,
                pageSize: data.pageSize,
                lastPage: data.lastPage,
                pageNumber: data.pageNumber
            })

            console.log(data);

        }).catch(error => {
            toast.error("Error in loading posts")

        })
    }



    function deletePost(post) {
        //going to delete post
        console.log(post)

        deletePostService(post.postId).then(res => {
            console.log(res)
            toast.success("post is deleled..")

            let newPostContents = postContent.content.filter(p => p.postId != post.postId)
            setPostContent({ ...postContent, content: newPostContents })

        })
            .catch(error => {
                console.log(error)
                toast.error("error in deleting post")
            })
    }


    const changePageInfinite = () => {
        console.log("page chagned")
        setCurrentPage(currentPage + 1)

    }

    return (
        <div className="container-fluid">
            <Row>
                <Col md={
                    {
                        size: 12

                    }
                }>

                    <h1>Total Posts ( {postContent?.totalElements} )</h1>
                    <div className="flex items-center mb-2 mt-3 w-1/2">
                        <Input
                            type="text"
                            placeholder="Enter keyword"
                            value={keyword}
                            onChange={e => setKeyword(e.target.value)}
                        />
                        <Button className="ml-2" color="primary" onClick={searchPostsByKeyword}>Search</Button>
                    </div>
                    <InfiniteScroll
                        dataLength={postContent.content.length}
                        next={changePageInfinite}
                        hasMore={!postContent.lastPage}
                        loader={<h4>Loading...</h4>}
                        endMessage={
                            <p style={{textAlign: 'center'}}>
                                <b>Yay! You have seen it all</b>
                            </p>
                        }
                    >
                        {
                            postContent.content.map((post, index) => (
                                <Post deletePost={deletePost} post={post} key={index}/>
                            ))
                        }

                    </InfiniteScroll>

                </Col>
            </Row>
        </div>


    )
}

export default NewFeed