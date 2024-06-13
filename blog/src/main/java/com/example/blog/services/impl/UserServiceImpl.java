package com.example.blog.services.impl;

import com.example.blog.entities.Role;
import com.example.blog.entities.User;
import com.example.blog.entities.VerificationToken;
import com.example.blog.exceptions.ResourceNotFoundException;
import com.example.blog.exceptions.UserNameIsExistsException;
import com.example.blog.payloads.UserDto;
import com.example.blog.repositories.RoleRepo;
import com.example.blog.repositories.UserRepo;
import com.example.blog.repositories.VerificationTokenRepo;

import com.example.blog.services.FileService;
import com.example.blog.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationTokenRepo tokenRepo;

    @Autowired
    private RoleRepo roleRepo;
    @Autowired
    private FileService fileService;
    @Autowired
    private JavaMailSender javaMailSender;
    @Override
    public UserDto createUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        user.setAvatarUrl("dfavatar.png");
        User savedUser = userRepo.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }



    @Override
    public UserDto updateUser(UserDto userDto,Integer userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","id",userId));
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setAbout(userDto.getAbout());
        User savedUser = userRepo.save(user);
        return modelMapper.map(savedUser, UserDto.class);
}

    @Override
    public UserDto getUserById(Integer userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","id",userId));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users=userRepo.findAll();
        List<UserDto> userDtos = users.stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
        return userDtos;
    }

    @Override
    public void deleteUser(Integer userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","id",userId));
        userRepo.delete(user);
    }

    @Override
    public User register(UserDto userDto) {
        String userName = userDto.getEmail();
        Optional<User> user = userRepo.findByEmail(userName);
        if(user.isPresent()) throw new UserNameIsExistsException(userName);

        User user1 = modelMapper.map(userDto,User.class);
        Role role = roleRepo.findById(501).get();
        user1.getRoles().add(role);
        user1.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return userRepo.save(user1);
    }



    @Override
    public void saveUserVerificationToken(User theUser, String token) {
        VerificationToken verificationToken = new VerificationToken(token, theUser);
        tokenRepo.save(verificationToken);
    }




    public String validateToken(String theToken) {
        VerificationToken token = tokenRepo.findByToken(theToken);
        if(token == null){
            return "Invalid verification token";
        }
        User user = token.getUser();
        Calendar calendar = Calendar.getInstance();
        if ((token.getExpirationTime().getTime()-calendar.getTime().getTime())<= 0){
            return "Verification link already expired," +
                    " Please, click the link below to receive a new verification link";
        }
        user.setEnabled(true);
        userRepo.save(user);
        return "valid";
    }


    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = tokenRepo.findByToken(oldToken);
        var tokenExpirationTime = new VerificationToken();
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationToken.setExpirationTime(tokenExpirationTime.getTokenExpirationTime());
        return tokenRepo.save(verificationToken);
    }

    @Override
    public String updateAvatarUrl(Integer userId, MultipartFile file) throws IOException {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","userId",userId));
        String url = fileService.uploadImage("Avatar/",file);
        user.setAvatarUrl(url);
        userRepo.save(user);
        return url;
    }

    @Override
    public String getAvatarUrl(Integer userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","userId",userId));
        String url = user.getAvatarUrl();
        return url;
    }

    @Override
    public Map<String, Object> changePass(Map<String, String> request) {
        Map<String,Object> response = new HashMap<>();
        try {
            Integer userId = Integer.valueOf(request.get("userId"));
            String oldPass = request.get("old");
            String newPass = request.get("new");
            User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User","userId",userId));

            if(passwordEncoder.matches(oldPass,user.getPassword())) {
                user.setPassword(passwordEncoder.encode(newPass));
                userRepo.save(user);
                response.put("message","Password is changged successfully");
                response.put("status",true);
                return response;
            }
            else {
                response.put("message","Password is'nt changged");
                response.put("status",false);
            }

        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            response.put("message","Check arg sended to server");
            response.put("status",false);
        }

        return response;
    }

    @Override
    public Map<String, Object> forgotPass(Map<String, String> request) {
        Map <String,Object> response = new HashMap<>();
        try {
            String gmail = request.get("gmail");
            User user = userRepo.findByEmail(gmail).orElseThrow(() -> new ResourceNotFoundException("User","gmail",0));
            String newPass = generateNewPass();
            user.setPassword(passwordEncoder.encode(newPass));
            userRepo.save(user);
            SimpleMailMessage mss = new SimpleMailMessage();
            mss.setFrom("tan31052003@gmail.com");
            mss.setSubject("New Password for your account");
            mss.setTo(gmail);
            mss.setText(newPass);
            javaMailSender.send(mss);
            response.put("message","Password is sended to your gmail");
            response.put("status",true);

        } catch (NumberFormatException ex) {
            ex.printStackTrace();
            response.put("message","Check arg sended to server");
            response.put("status",false);
        }
        return response;
    }

    private String generateNewPass() {
        String newpass = UUID.randomUUID().toString().substring(0,6);
        return newpass;
    }


}
