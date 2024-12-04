package com.henrytran1803.BEBakeManage.user.dto;

import com.henrytran1803.BEBakeManage.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;
@Getter
@Setter
public class UserResponseRegisterDTO {

        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String dateOfBirth;
        private Boolean isActive;
        private Set<Long> roleIds; // Chỉ trả về tên các role

        public UserResponseRegisterDTO(Long id, String firstName, String lastName, String email, String dateOfBirth, Boolean isActive, Set<Long> roles) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.email = email;
            this.dateOfBirth=dateOfBirth;
            this.isActive = isActive;
            this.roleIds = roles;
        }

    public UserResponseRegisterDTO(User user) {
    }

    // Getters và setters


}
