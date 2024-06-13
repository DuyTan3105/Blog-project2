package com.example.blog.services;

import com.example.blog.entities.Comment;
import com.example.blog.payloads.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createCommment(CommentDto commentDto, Integer postId, Integer userId, Integer commentId);
    void deleteComment(Integer commentId);

    List<CommentDto> getReplies(Integer commentId);
    List<CommentDto> getComment(Integer postId);


}
