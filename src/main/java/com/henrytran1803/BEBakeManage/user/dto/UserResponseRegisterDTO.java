package com.henrytran1803.BEBakeManage.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;
@Getter
@Setter
public class UserResponseRegisterDTO {

        private int id;
        private String firstName;
        private String lastName;
        private String email;
        private String dateOfBirth;
        private Boolean isActive;
        private Set<String> roles; // Chỉ trả về tên các role

        public UserResponseRegisterDTO(int id, String firstName, String lastName, String email, String dateOfBirth, Boolean isActive, Set<String> roles) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.dateOfBirth=dateOfBirth;
            this.isActive = isActive;
            this.roles = roles;
        }

        // Getters và setters


}
