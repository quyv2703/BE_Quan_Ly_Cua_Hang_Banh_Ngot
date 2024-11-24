package com.henrytran1803.BEBakeManage.user.dto;

import com.henrytran1803.BEBakeManage.user.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.Set;

public class RegisterRequest {
    @NotBlank(message = "{first.name.not.blank}")
    private String firstName;

    @NotBlank(message = "{last.name.not.blank}")
    private String lastName;

    @Email(message = "{email.invalid.format}")
    @NotBlank(message = "{email.not.blank}")
    private String email;

    @NotNull(message = "{date.of.birth.not.null}")
    private String dateOfBirth;

    @NotBlank(message = "{password.not.blank}")
    private String password;

    @NotNull(message = "{roles.not.null}")
    private Set<String> roles;

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    // Getters and Setters
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
