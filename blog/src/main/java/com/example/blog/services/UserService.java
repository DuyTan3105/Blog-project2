package com.example.blog.services;

import com.example.blog.entities.User;
import com.example.blog.entities.VerificationToken;
import com.example.blog.payloads.UserDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface UserService {
    UserDto createUser(UserDto user);
    UserDto updateUser(UserDto user,Integer userId);
    UserDto getUserById(Integer userId);
    List<UserDto> getAllUsers();
    void deleteUser(Integer userId);

    User register(UserDto userDto);


    void saveUserVerificationToken(User theUser, String verificationToken);


    String validateToken(String token);

    VerificationToken generateNewVerificationToken(String oldToken);

    String updateAvatarUrl(Integer userId, MultipartFile file) throws IOException;

    String getAvatarUrl(Integer userId);


    Map<String, Object> changePass(Map<String, String> request);

    Map<String, Object> forgotPass(Map<String, String> request);
}
