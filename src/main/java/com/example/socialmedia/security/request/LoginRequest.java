package com.example.socialmedia.security.request;

import lombok.Data;

@Data
public class LoginRequest {

    private String username;
    private String password;
}
