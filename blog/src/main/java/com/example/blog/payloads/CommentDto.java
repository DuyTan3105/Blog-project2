package com.example.blog.payloads;

import com.example.blog.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {
    private Integer id;
    private String content;
    private Date creationDate;
    private Integer postId;
    private UserDto user;
    @Override
    public String toString() {
        return String.format("id: %s, content: %s, postId: %s",getId(),getContent(),getPostId());
    }
}
