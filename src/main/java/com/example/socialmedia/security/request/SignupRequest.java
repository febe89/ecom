package com.example.socialmedia.security.request;

import com.example.socialmedia.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
public class SignupRequest {

    private String username;
    private String email;
    private String password;

    public SignupRequest(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    private Set<String> roles;

}
