package com.example.blog.services.impl;

import com.example.blog.entities.*;
import com.example.blog.exceptions.ResourceNotFoundException;
import com.example.blog.payloads.PostDto;
import com.example.blog.payloads.PostResponse;
import com.example.blog.repositories.*;
import com.example.blog.services.PostService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PostServiceImpl implements PostService {
    @Autowired
    private PostRepo postRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private CategoryRepo categoryRepo;
    @Autowired
    private PostHistoryRepo postHistoryRepo;
    @Autowired
    private PostAnalyticsRepo postAnalyticsRepo;
    @Autowired
    private CommentRepo commentRepo;


    @Override
    public PostDto createPost(PostDto postDto, Integer userId, Integer categoryId) {
        User user = userRepo.findById(userId).orElseThrow(() ->
            new ResourceNotFoundException("user","userId",userId)
        );
        Category category=categoryRepo.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("category","categoryId",categoryId));

        Post post = modelMapper.map(postDto, Post.class);
        post.setAddedDate(new Date());
        post.setUser(user);
        post.setCategory(category);
        Post newPost = postRepo.save(post);
        Postanalytics postanalytics = new Postanalytics();
        postanalytics.setPost(newPost);
        postAnalyticsRepo.save(postanalytics);
        return modelMapper.map(newPost, PostDto.class);
    }


    @Override
    public PostDto updatePost(PostDto postDto, Integer postId) {
        Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","postId",postId));
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setImageUrl(postDto.getImageUrl());
        post.setAddedDate(postDto.getAddedDate());

        Post updatePost = postRepo.save(post);
        return modelMapper.map(updatePost,PostDto.class);

    }

    @Override
    public void deletePost(Integer postId) {
        Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","postId",postId));
        postRepo.delete(post);

    }

    @Override
    public PostResponse getAllPost(Integer pageNumber, Integer pageSize, String sortBy, String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc")? Sort.by(sortBy).ascending(): Sort.by(sortBy).descending();

        Pageable p = PageRequest.of(pageNumber,pageSize, sort);
        Page<Post> pagePost = postRepo.findAll(p);
        List<Post> posts = pagePost.getContent();
        List<PostDto> postDtos= posts.stream().map(post -> modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
        PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageNumber(pagePost.getNumber());
        postResponse.setPageSize(pagePost.getSize());
        postResponse.setTotalElements(pagePost.getTotalElements());
        postResponse.setTotalPages(pagePost.getTotalPages());
        postResponse.setLastPage(pagePost.isLast());
        return postResponse;
    }

    @Override
    public PostDto getPostById(Integer postId) {
        Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","postId",postId));
        return modelMapper.map(post,PostDto.class);
    }

    @Override
    public List<PostDto> getPostByCategory(Integer categoryId) {
        Category cat = categoryRepo.findById(categoryId).orElseThrow(() ->
                new ResourceNotFoundException("Category","categoryID",categoryId));
        List<Post> posts = postRepo.findByCategory(cat);
        List<PostDto> postDtos=posts.stream().map(post -> modelMapper.map(post,PostDto.class)).collect(Collectors.toList());
        return postDtos;
    }

    @Override
    public List<PostDto> getPostByUser(Integer userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","userId",userId));
        List<Post> users = postRepo.findByUser(user);
        List<PostDto> userDtos = users.stream().map(post -> modelMapper.map(post, PostDto.class)).collect(Collectors.toList());
        return userDtos;
    }

    @Override
    public PostResponse searchPosts(String keyword, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize);
        Page<Post> pageposts = postRepo.findByTitleContaining(keyword,pageable);
        List<Post> posts = pageposts.getContent();
        log.info("{}",posts.size());
        List<PostDto> postDtos = posts.stream().map(post -> modelMapper.map(post,PostDto.class)).collect(Collectors.toList())
                ;PostResponse postResponse = new PostResponse();
        postResponse.setContent(postDtos);
        postResponse.setPageNumber(pageposts.getNumber());
        postResponse.setPageSize(pageposts.getSize());
        postResponse.setTotalElements(pageposts.getTotalElements());
        postResponse.setTotalPages(pageposts.getTotalPages());
        postResponse.setLastPage(true);
        return postResponse;
    }

    @Override
    public Map<String, Object> interactPost(Integer postId, Integer userId, String type) {
        Post post = postRepo.findById(postId).orElseThrow(() -> new ResourceNotFoundException("Post","postId",postId));
        Postanalytics postanalytics = postAnalyticsRepo.findByPost(post).orElseThrow(() -> new ResourceNotFoundException("Post","postId",post.getPostId()));
        Map<String,Object> res = new HashMap<>();

        if (type != null) {
            Optional<PostHistory> postHistoryOptional = postHistoryRepo.findByPostIdAndUserIdAndType(postId, userId, PostHistory.Type.valueOf(type));
            if (postHistoryOptional.isPresent()) {
                switch (type) {
                    case "LIKE":
                        if (postanalytics.getLikes() > 0) {
                            postanalytics.setLikes(postanalytics.getLikes() - 1);
                            postAnalyticsRepo.save(postanalytics);
                        }
                        break;
                    case "SHARE":
                        if (postanalytics.getShares() > 0) {
                            postanalytics.setShares(postanalytics.getShares() - 1);
                            postAnalyticsRepo.save(postanalytics);
                        }
                        break;
                    default:
                        res.put("Type","Invalid");
                        return res;
                }
                postHistoryRepo.delete(postHistoryOptional.get());
                res.put("Message","Interaction canceled successfully.");
            } else {
                switch (type) {
                    case "LIKE":
                        postanalytics.setLikes(postanalytics.getLikes() + 1);
                        break;
                    case "SHARE":
                        postanalytics.setShares(postanalytics.getShares() + 1);
                        break;
                    default:
                        res.put("Type","Invalid");
                        return res;
                }
                postAnalyticsRepo.save(postanalytics);
                PostHistory postHistory = new PostHistory();
                postHistory.setPostId(postId);
                postHistory.setUserId(userId);
                postHistory.setAttime(new Date());
                postHistory.setType(PostHistory.Type.valueOf(type));
                postHistoryRepo.save(postHistory);
                res.put("Message","Interaction added successfully.");
            }
        }

        return res;
    }


    @Override
    public Map<String, Object> getAllInteractions(Integer postId) {
        Post post = postRepo.findById(postId).orElseThrow();
        Postanalytics postanalytics = postAnalyticsRepo.findByPost(post).orElseThrow();
        int comments = commentRepo.findAllByPostId(postId).orElseThrow().size();
        Map<String,Object> res = new HashMap<>();
        res.put("likes",postanalytics.getLikes());
        res.put("comments",comments);
        res.put("shares",postanalytics.getShares());
        return res;
    }

    @Override
    public Map<String, Object> checkliked(Integer postId, Integer userId) {
        Boolean bool = postHistoryRepo.existsByPostIdAndUserIdAndType(postId,userId,PostHistory.Type.LIKE);
        Map<String,Object> res = new HashMap<>();
        res.put("checked",bool);
        return res;
    }


}
