package com.example.blog.services;

import com.example.blog.entities.PostHistory;
import com.example.blog.payloads.PostDto;
import com.example.blog.payloads.PostResponse;

import java.util.List;
import java.util.Map;

public interface PostService {
    PostDto createPost(PostDto postDto,Integer userId,Integer categoryId);

    PostDto updatePost(PostDto postDto, Integer postId);
    void deletePost(Integer postId);
    PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir);
    PostDto getPostById(Integer postId);
    List<PostDto> getPostByCategory(Integer categoryId);
    List<PostDto> getPostByUser(Integer userId);
    PostResponse searchPosts(String keyword, Integer pageNumber, Integer pageSize);


    Map<String, Object> interactPost(Integer postId, Integer userId, String type);

    Map<String, Object> getAllInteractions(Integer postId);


    Map<String, Object> checkliked(Integer postId, Integer userId);
}
