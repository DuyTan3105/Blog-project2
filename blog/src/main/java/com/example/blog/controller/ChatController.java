package com.example.blog.controller;


import com.example.blog.payloads.MessageBean;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import org.springframework.messaging.handler.annotation.MessageMapping;


@Controller
public class ChatController {

    @MessageMapping("/user-all")
    @SendTo("/topic/user")
    public MessageBean send(@Payload MessageBean message) {
        return message;
    }
}

