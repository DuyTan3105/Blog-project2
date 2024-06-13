package com.example.blog.controller;


import com.example.blog.entities.Comment;
import com.example.blog.payloads.ApiResponse;
import com.example.blog.payloads.CommentDto;
import com.example.blog.payloads.UserDto;
import com.example.blog.services.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comments")
@Slf4j
public class CommentController {
    @Autowired
    private CommentService commentService;

    @PostMapping("/post/{postId}/{userId}")
    public ResponseEntity<CommentDto> createComment(@RequestBody CommentDto commentDto,
                                                    @PathVariable("postId") Integer postId,
    @PathVariable("userId") Integer userId,
    @RequestParam(required = false,defaultValue = "0",name = "commentId") Integer commentId) {
        log.info(commentDto.toString());
        CommentDto newComment = commentService.createCommment(commentDto,postId,userId,commentId);
        return new ResponseEntity<>(newComment, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<ApiResponse> deleteComment(@PathVariable Integer commentId) {
        commentService.deleteComment(commentId);
        return new ResponseEntity<>(new ApiResponse("Comment is successfully deleted",true),HttpStatus.OK);
    }
    @GetMapping("/get/{postId}")
    public ResponseEntity<List<CommentDto>> getComment(@PathVariable Integer postId) {
        List<CommentDto> comments = commentService.getComment(postId);
        return new ResponseEntity<>(comments,HttpStatus.OK);
    }

    @GetMapping("/get-replies/{commentId}")
    public ResponseEntity<List<CommentDto>> getReplies(@PathVariable Integer commentId) {
        List<CommentDto> commentDtos= commentService.getReplies(commentId);
        return ResponseEntity.ok(commentDtos);
}
}

