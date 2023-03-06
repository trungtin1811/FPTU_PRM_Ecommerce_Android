package com.example.ecommerceproject.entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.ecommerceproject.enums.Role;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
@Entity(tableName = "accounts", indices = @Index(value = "email", unique = true))
public class Account {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    public String id;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "email")
    public String email;
    @ColumnInfo(name = "image_url")
    public String imageUrl;
    @ColumnInfo(name = "address")
    public String address;
    @ColumnInfo(name = "phone")
    public String phone;
    @ColumnInfo(name = "role")
    public Role role;
}
