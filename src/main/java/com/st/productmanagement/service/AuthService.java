package com.st.productmanagement.service;

import com.st.productmanagement.dtos.LoginRequestDto;
import com.st.productmanagement.dtos.AuthResponseDto;
import com.st.productmanagement.dtos.SignUpRequestDto;
import com.st.productmanagement.dtos.UserResponseDto;
import com.st.productmanagement.entity.User;
import com.st.productmanagement.exception.InvalidCredentialsException;
import com.st.productmanagement.exception.UserNotFoundException;
import com.st.productmanagement.repository.UserRepository;
import com.st.productmanagement.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtUtil jwtUtil;
    @Autowired
    private final UserRepository userRepository;
     public UserResponseDto saveUser(SignUpRequestDto ur){

         User user = new User();
         user.setName(ur.getName());
         user.setRole(ur.getRole());
         user.setEmail(ur.getEmail());
         user.setPassword(ur.getPassword());
           userRepository.save(user);
          UserResponseDto userResponseDto = new UserResponseDto();
          userResponseDto.setName(user.getName());
          userResponseDto.setEmail(user.getEmail());
          userResponseDto.setRole(user.getRole());
          return  userResponseDto;
     }
      public AuthResponseDto login(LoginRequestDto dto){
          User user = userRepository.findByEmail(dto.getEmail());

          if(user == null){
              throw new UserNotFoundException("User not found");
          }

          if(!user.getPassword().equals(dto.getPassword())){
              throw new InvalidCredentialsException("Invalid password");
          }
          String accessToken = jwtUtil.generateAccessToken(user);
          String refreshToken = jwtUtil.generateRefreshToken(user);

          UserResponseDto userResponse = new UserResponseDto();
          userResponse.setName(user.getName());
          userResponse.setEmail(user.getEmail());
          userResponse.setRole(user.getRole());

          AuthResponseDto response = new AuthResponseDto();
          response.setAccessToken(refreshToken);
          response.setRefreshToken(accessToken);
          response.setUser(userResponse);
          return response;
      }
}
