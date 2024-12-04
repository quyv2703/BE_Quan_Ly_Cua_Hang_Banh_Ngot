package com.henrytran1803.BEBakeManage.user.dto;

import com.henrytran1803.BEBakeManage.user.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Set;
@Getter
@Setter
public class RegisterRequest {
    @NotBlank(message = "{first.name.not.blank}")
    private String firstName;

    @NotBlank(message = "{last.name.not.blank}")
    private String lastName;

    @Email(message = "{email.invalid.format}")
    @NotBlank(message = "{email.not.blank}")
    private String email;

    @NotNull(message = "{date.of.birth.not.null}")
    private Date dateOfBirth;

    @NotBlank(message = "{password.not.blank}")
    private String password;

    @NotNull(message = "{roles.not.null}")
    private List<Integer> roleIds;
}
