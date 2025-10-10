package com.example.demo.dto.response;

import com.example.demo.dto.UserDto;

public class TokenResponse {
    public String accessToken;
    public String tokenType = "Bearer";
    public long expiresInSec;
    public UserDto user;
}