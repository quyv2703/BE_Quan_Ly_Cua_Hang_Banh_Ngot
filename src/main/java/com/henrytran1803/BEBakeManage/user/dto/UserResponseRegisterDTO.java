package com.henrytran1803.BEBakeManage.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;
@Getter
@Setter
public class UserResponseRegisterDTO {

        private int id;
        private String firstName;
        private String lastName;
        private String email;
        private Boolean isActive;
        private Set<String> roles; // Chỉ trả về tên các role

        public UserResponseRegisterDTO(int id, String firstName, String lastName, String email, Boolean isActive, Set<String> roles) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.isActive = isActive;
            this.roles = roles;
        }

        // Getters và setters


}
