package com.example.blog.payloads;

import com.example.blog.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private int id;
    @NotEmpty
    @Size(min=4, message = "Username must be min of 4 characters !!")
    private String name;
    @Email(message = "Email address is not valid !!")
    private String email;
    @NotEmpty
    @Size(min = 3, max=10, message = "password must be min of 3 and max of 10")
    private String password;
    @NotEmpty
    private String about;
    private String avatarUrl;
    private Set<RoleDto> roles = new HashSet<>();
    private User.Gender gender;
    private Date dob;

    public UserDto(User user) {
        id=user.getId();
        name=user.getName();
        avatarUrl=user.getAvatarUrl();
    }
}
