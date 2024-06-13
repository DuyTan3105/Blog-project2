package com.example.blog.events.listener;

import com.example.blog.entities.User;
import com.example.blog.events.CommentCompleteEvent;
import org.springframework.context.ApplicationListener;

public class CommentCompleteListener implements ApplicationListener<CommentCompleteEvent> {


    @Override
    public void onApplicationEvent(CommentCompleteEvent event) {
        User theUser = event.getUser();

    }
}
