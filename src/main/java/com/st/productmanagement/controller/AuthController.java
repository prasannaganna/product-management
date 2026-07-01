package com.st.productmanagement.controller;

import com.st.productmanagement.dtos.LoginRequestDto;
import com.st.productmanagement.dtos.AuthResponseDto;
import com.st.productmanagement.dtos.SignUpRequestDto;
import com.st.productmanagement.dtos.UserResponseDto;
import com.st.productmanagement.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public UserResponseDto userRegister(@Valid @RequestBody SignUpRequestDto ur){
        return authService.saveUser(ur);
    }
    @PostMapping("/login")
    public AuthResponseDto login(@Valid @RequestBody LoginRequestDto dto) {
        return authService.login(dto);

    }
}
