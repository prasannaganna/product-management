package com.st.productmanagement.dtos;

import com.st.productmanagement.enums.Role;
import lombok.Data;

@Data
public class UserResponseDto {
     private String  name;
     private  String email;
     private Role role;
}
