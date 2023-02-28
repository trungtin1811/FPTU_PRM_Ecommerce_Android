package com.example.ecommerceproject.dto;

import com.example.ecommerceproject.entities.Account;
import com.example.ecommerceproject.enums.Role;

import java.io.Serializable;

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
public class AccountModel implements Serializable {
    public String id;
    public String name;
    public String email;
    public Role role;

    public AccountModel(Account account) {
        this.id = account.getId();
        this.name = account.getName();
        this.email = account.getEmail();
        this.role = account.getRole();
    }
}
