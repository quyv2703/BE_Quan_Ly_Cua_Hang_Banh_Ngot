package com.henrytran1803.BEBakeManage.user.dto;

import com.henrytran1803.BEBakeManage.user.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Optional;

@Data
public class LoginResponse {
    private UserDTO user;
    private String token;

    public LoginResponse(UserDTO user, String token) {
        this.user = user;
        this.token = token;
    }
}