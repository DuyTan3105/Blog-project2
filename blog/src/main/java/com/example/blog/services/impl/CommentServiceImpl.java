package com.example.blog.services.impl;

import com.example.blog.entities.Comment;
import com.example.blog.entities.Postanalytics;
import com.example.blog.entities.User;
import com.example.blog.exceptions.ResourceNotFoundException;
import com.example.blog.payloads.CommentDto;
import com.example.blog.payloads.UserDto;
import com.example.blog.repositories.CommentRepo;
import com.example.blog.repositories.PostAnalyticsRepo;
import com.example.blog.repositories.PostRepo;
import com.example.blog.repositories.UserRepo;
import com.example.blog.services.CommentService;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private PostAnalyticsRepo postAnalyticsRepo;
    @Autowired
    private CommentRepo commentRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public CommentDto createCommment(CommentDto commentDto, Integer postId, Integer userId, Integer commentId) {
        Comment comment = modelMapper.map(commentDto,Comment.class);
        comment.setPostId(postId);
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","UserId",userId));
        comment.setUser(user);
        if(commentId==0)
        comment.setParentComment(null);
        else
        {
            Comment parent = commentRepo.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("comment","commentId",commentId));
            parent.getReplies().add(comment);
            comment.setParentComment(parent);
        }
        comment.setCreationDate(new Date());
        Comment savedComment = commentRepo.save(comment);

        return new CommentDto(savedComment.getId(),savedComment.getContent(),savedComment.getCreationDate(),savedComment.getPostId(),new UserDto(savedComment.getUser()));
    }

    @Override
    public void deleteComment(Integer commentId) {
        Comment comment = commentRepo.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment","CommentId",commentId));
        commentRepo.delete(comment);
    }

    @Override
    public List<CommentDto> getReplies(Integer commentId) {
        // TODO Auto-generated method stub
        List<Comment> comments = commentRepo.findAllParent(commentId).orElseThrow(() -> new ResourceNotFoundException("comments","replies",commentId));
        return comments.stream().map(comment -> new CommentDto(comment.getId(),
                comment.getContent(),
                comment.getCreationDate(),
                comment.getPostId(),
                new UserDto(comment.getUser()))).collect(Collectors.toList());
    }
    @Override
    public List<CommentDto> getComment(Integer postId) {
        List<Comment> comment = commentRepo.findAllByPostId(postId).orElseThrow(() -> new ResourceNotFoundException("Comment","PostId",postId));
        List<CommentDto> commentDtos=comment.stream().map(comment1 -> new CommentDto(comment1.getId(),
                comment1.getContent(),
                comment1.getCreationDate(),
                comment1.getPostId(),
                new UserDto(comment1.getUser()))).collect(Collectors.toList());
      return commentDtos;
    }


}
