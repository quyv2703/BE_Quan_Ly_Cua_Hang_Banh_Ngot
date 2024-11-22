package com.henrytran1803.BEBakeManage.user.dto;

import com.henrytran1803.BEBakeManage.user.ValidationMessages;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.Set;
@Getter
@Setter
public class CreateUserRequest {
    @NotBlank(message = "{first.name.not.blank}")
    private String firstName;

    @NotBlank(message = "{last.name.not.blank}") // Dùng key trong file messages.properties
    private String lastName;

    @Email(message = "{email.invalid.format}") // Dùng key trong file messages.properties
    @NotBlank(message = "{email.not.blank}")
    private String email;

    @NotBlank(message = "{password.not.blank}")
    private String password;

    @NotNull(message = "{date.of.birth.not.null}")
    private Date dateOfBirth;

    @NotNull(message = "{active.status.not.null}")
    private Boolean isActive;

    @NotNull(message = "{roles.not.null}")
    private Set<String> roles;


    // Getters và Setters
}
