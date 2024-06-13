package com.example.blog.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@Entity
@Table(name = "post_history")
public class PostHistory {
    public enum Type {
        LIKE,SHARE,COMMENT
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer postId;
    private Integer userId;
    private Date attime;
    @Enumerated(EnumType.STRING)
    private Type type;


}
