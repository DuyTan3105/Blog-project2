package com.example.blog.events;

import com.example.blog.entities.Comment;
import com.example.blog.entities.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class CommentCompleteEvent extends ApplicationEvent {
    private User user;
    private String applicationUrl;
   public CommentCompleteEvent(User user,String applicationUrl) {
       super(user);
       this.user=user;
       this.applicationUrl=applicationUrl;
   }
}
