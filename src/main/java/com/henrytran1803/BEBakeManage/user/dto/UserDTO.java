package com.henrytran1803.BEBakeManage.user.dto;

import com.henrytran1803.BEBakeManage.user.entity.Role;

import java.util.Set;
import java.util.stream.Collectors;

public class UserDTO {
    private int id;
    private String email;
    private Set<String> roles;
    public UserDTO(int id, String email, Set<String> roles) {
        this.id = id;
        this.email = email;
        this.roles = roles;

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getRoles() {
        return roles;
    }
}
