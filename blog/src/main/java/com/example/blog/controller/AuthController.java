package com.example.blog.controller;

import com.example.blog.entities.User;
import com.example.blog.entities.VerificationToken;
import com.example.blog.events.RegistrationCompleteEvent;
import com.example.blog.events.listener.RegistrationCompleteEventListener;
import com.example.blog.payloads.JwtAuthRequest;
import com.example.blog.payloads.JwtAuthResponse;
import com.example.blog.payloads.UserDto;
import com.example.blog.repositories.VerificationTokenRepo;
import com.example.blog.security.JwtTokenHelper;
import com.example.blog.services.UserService;
import com.example.blog.utils.applicationUrl;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;

import org.springframework.http.ResponseEntity;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;



import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {
    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ModelMapper mapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private RegistrationCompleteEventListener eventListener;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private VerificationTokenRepo tokenRepo;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtAuthRequest request) {
        log.info("{}, {}",request.getUsername(),request.getPassword());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtTokenHelper.generateToken(userDetails);
        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(token);
        response.setUser(this.mapper.map((User) userDetails, UserDto.class));
        return ResponseEntity.ok(response);
    }
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody UserDto userDto,final HttpServletRequest request) {
        System.out.println("User :"+userDto);
        User newUser = userService.register(userDto);
        publisher.publishEvent(new RegistrationCompleteEvent(newUser, applicationUrl.apply(request)));
        return ResponseEntity.ok("Success!  Please, check your email for to complete your registration");
    }


    @GetMapping("/verifyEmail")
    public String sendVerificationToken(@RequestParam("token") String token,final HttpServletRequest request){
        VerificationToken theToken = tokenRepo.findByToken(token);

        if (theToken.getUser().isEnabled()){
            return "This account has already been verified, please, login.";
        }
        String verificationResult = userService.validateToken(token);
        if (verificationResult.equalsIgnoreCase("valid")){
        return "Email verified successfully. Now you can login to your account";
    }
        String url = applicationUrl.apply(request)+"/api/auth/resend-verification-token?token="+token;
        return "Invalid verification link, please, " + "<a href=\"" + url + "\">Verify your email to activate your account</a>";
}

    @GetMapping("/resend-verification-token")
    public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request)
            throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl.apply(request)+"api/auth/resend-verification-token?token="+oldToken;
        VerificationToken verificationToken =  userService.generateNewVerificationToken(oldToken);
        User theUser = verificationToken.getUser();
        resendVerificationTokenEmail(theUser, applicationUrl.apply(request), verificationToken);
        return "A new verification link hs been sent to your email," +
                " please, check to complete your registration";
    }

    private void resendVerificationTokenEmail(User theUser, String applicationUrl, VerificationToken token)
            throws MessagingException, UnsupportedEncodingException {
        String url = applicationUrl+"/api/auth/verifyEmail?token="+token.getToken();
        eventListener.sendVerificationEmail(url);
        log.info("Click the link to verify your registration :  {}", url);
    }

    @PutMapping("/changePass")
    public ResponseEntity<Map<String,Object>> changgingPass(@RequestBody Map<String,String> request) {
       Map<String,Object> response = userService.changePass(request);
       return ResponseEntity.ok(response);
    }

    @PostMapping("/forgotPass")
    public ResponseEntity<Map<String,Object>> forgotPass(@RequestBody HashMap<String,String> request) {
        log.info(request.get("gmail"));
        Map<String,Object> response = userService.forgotPass(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/changePass")
    public ResponseEntity<Map<String,Object>> changePass (@RequestBody Map<String,String> request) {
        Map<String,Object> res = userService.changePass(request);
        return ResponseEntity.ok(res);
    }



}
