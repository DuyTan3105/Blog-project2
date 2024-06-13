package com.example.blog.exceptions;

public class UserNameIsExistsException extends RuntimeException{
    String userName;
    public UserNameIsExistsException(String userName) {
        super(String.format("Accconunt is exist with email: %s",userName));
        this.userName=userName;
    }
}
