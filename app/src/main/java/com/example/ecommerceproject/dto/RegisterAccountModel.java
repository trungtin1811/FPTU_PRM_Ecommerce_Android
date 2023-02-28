package com.example.ecommerceproject.dto;

import com.example.ecommerceproject.entities.Account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class RegisterAccountModel {
    private String email;
    private String password;
    private String name;
    private String confirmPassword;
}
