package com.example.demo.security;

import com.example.demo.dto.UserDto;
import com.example.demo.resource.entity.User;

public class AuthMapper {
    public static UserDto toDto(User u) {
        var d = new UserDto();
        d.id = u.getId().toString();
        d.name = u.getName();
        d.email = u.getEmail();
        d.role = u.getRole().name();
        return d;
    }
}