package com.example.blog.controller;

import com.example.blog.payloads.ApiResponse;
import com.example.blog.payloads.UserDto;
import com.example.blog.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping("/add")
    public ResponseEntity<UserDto> createUsero(@Valid @RequestBody UserDto userDto) {
        UserDto createUserDto = userService.createUser(userDto);
        return new ResponseEntity<>(createUserDto, HttpStatus.CREATED);
    }

    @PutMapping("/update/avatar/{userId}")
    public ResponseEntity<String> updateAvatar(@PathVariable Integer userId,
                                            @RequestParam MultipartFile file) throws IOException {
        String url = userService.updateAvatarUrl(userId,file);
        return new ResponseEntity<>(url,HttpStatus.OK);
    }
    @GetMapping("/get/avatar/{userId}")
    public ResponseEntity<String> getAvatar(@PathVariable Integer userId) {
        String url = userService.getAvatarUrl(userId);
        return ResponseEntity.ok(url);
    }

    @PutMapping("update/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto, @PathVariable("userId") Integer userId) {
        UserDto updateUser = userService.updateUser(userDto,userId);
        return ResponseEntity.ok(updateUser);
    }
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable("userId") Integer uid) {
        userService.deleteUser(uid);
        return new ResponseEntity(new ApiResponse("User deleted Successfully",true),HttpStatus.OK);
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<UserDto> getUserById (@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @GetMapping("/get-all-users")
    public ResponseEntity<List<UserDto>> getAllUsers () {
        return ResponseEntity.ok(userService.getAllUsers());
    }


}
