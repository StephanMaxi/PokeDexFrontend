package com.example.demo.Models;

import lombok.Data;

@Data
public class SignInRequest {
    private String email;
    private String password;
}
