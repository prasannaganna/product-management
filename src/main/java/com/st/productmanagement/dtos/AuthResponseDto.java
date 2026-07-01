package com.st.productmanagement.dtos;

import lombok.Data;

@Data
public class AuthResponseDto {
    private  String accessToken;
    private  String refreshToken;
    UserResponseDto user;
}
