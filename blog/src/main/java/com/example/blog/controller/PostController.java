package com.example.blog.controller;
import com.example.blog.entities.PostHistory;
import com.example.blog.payloads.ApiResponse;
import com.example.blog.payloads.PostDto;
import com.example.blog.payloads.PostResponse;
import com.example.blog.services.FileService;
import com.example.blog.services.PostService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
@Slf4j
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private FileService fileService;

    @PostMapping("/add/{userId}/{categoryId}")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto,
                                              @PathVariable("userId") Integer userId,
                                              @PathVariable("categoryId") Integer categoryId) {
        log.info("PostDto: {}", postDto);
        PostDto newPost = postService.createPost(postDto, userId, categoryId);
        return new ResponseEntity<>(newPost, HttpStatus.CREATED);
    }

    @GetMapping("/user/{userId}/get")
    public ResponseEntity<List<PostDto>> getPostByUser(@PathVariable Integer userId) {
        List<PostDto> posts = postService.getPostByUser(userId);
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }

    @GetMapping("/category/{categoryId}/get")
    public ResponseEntity<List<PostDto>> getPostByCategory(@PathVariable Integer categoryId) {
        List<PostDto> posts = postService.getPostByCategory(categoryId);
        return new ResponseEntity<>(posts,HttpStatus.OK);
    }

    @GetMapping("/get-all-posts")
    public ResponseEntity<PostResponse> getAllPost(@RequestParam(value = "pageNumber",defaultValue = "0",required = false) Integer pageNumber,
                                                   @RequestParam(value = "pageSize",defaultValue = "1",required = false) Integer pageSize,
                                                   @RequestParam(value = "sortBy",defaultValue = "postId",required = false) String sortBy,
                                                   @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir) {
        PostResponse postResponse = postService.getAllPost(pageNumber,pageSize,sortBy,sortDir);
        return ResponseEntity.ok(postResponse);
    }

    @GetMapping("/get/{postId}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Integer postId) {
        PostDto postDto = postService.getPostById(postId);
        return ResponseEntity.ok(postDto);
    }

    @DeleteMapping("/delete/{postId}")
    public ResponseEntity<ApiResponse> deletePost(@PathVariable Integer postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok(new ApiResponse("Post is successfully deleted !!",true));
    }

    @PutMapping("/update/{postId}")
    public ResponseEntity<PostDto> updatePost(@RequestBody PostDto postDto, @PathVariable Integer postId) {
        PostDto updatedpost=postService.updatePost(postDto,postId);
        return ResponseEntity.ok(updatedpost);

    }

    @GetMapping("/search/{keywords}")
    public ResponseEntity<PostResponse> searchPostByTitle(
            @PathVariable("keywords") String keywords,
            @RequestParam(name = "pageNumber",defaultValue = "0",required = false) Integer pageNumber,
            @RequestParam(name = "pageSize",defaultValue = "10",required = false) Integer pageSize
    ) {
        log.info("{}",keywords);
        PostResponse res = postService.searchPosts(keywords,pageNumber,pageSize);
        log.info("res: {}",res.getContent().size());
        return new ResponseEntity<PostResponse>(res,HttpStatus.OK);
    }

    @PostMapping("/post/image/upload/{postId}")
    public ResponseEntity<PostDto> uploadPostImage(@RequestParam("image")MultipartFile image,
                                                         @PathVariable("postId") Integer postId) throws IOException {
        String fileName = fileService.uploadImage("Posts/",image);
        PostDto postDto = postService.getPostById(postId);
        postDto.setImageUrl(fileName);
        PostDto updatePost= this.postService.updatePost(postDto,postId);
        return new ResponseEntity<PostDto>(updatePost,HttpStatus.OK);
    }

    @GetMapping(value = "/post/image/{imageName}",produces = MediaType.IMAGE_JPEG_VALUE)
    public void downloadImage(
            @PathVariable("imageName") String imageName,
            HttpServletResponse response
    ) throws IOException {

        InputStream resource = this.fileService.getResource(imageName);
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream())   ;

    }

    @PutMapping("/interaction/{postId}/{userId}")
    public ResponseEntity<?> interactPost(@PathVariable Integer postId,
            @PathVariable Integer userId,
            @RequestParam(name = "type",required = true) String type) {
        log.info("{}","dulieu duoc gui den");
        Map<String ,Object> res = postService.interactPost(postId,userId,type);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/interaction/{postId}")
    public ResponseEntity<Map<String,Object>> getAllInteractions(@PathVariable Integer postId) {
        Map<String,Object> res = postService.getAllInteractions(postId);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/checkliked/{postId}/{userId}")
    public ResponseEntity<Map<String,Object>> checkLiked (@PathVariable Integer postId,
                                                          @PathVariable Integer userId) {
        Map<String,Object> res = postService.checkliked(postId,userId);
        return ResponseEntity.ok(res);
    }



}
