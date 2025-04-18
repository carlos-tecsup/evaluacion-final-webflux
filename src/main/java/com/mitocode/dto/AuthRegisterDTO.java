package com.mitocode.dto;



import java.util.List;

public record AuthRegisterDTO(
        String username,
        String password,
        List<String> roles
) {

}