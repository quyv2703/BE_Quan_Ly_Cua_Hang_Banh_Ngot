package com.henrytran1803.BEBakeManage.user.dto;

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
public class UserRequest {
    @NotBlank(message = "{first.name.not.blank}")
    private String firstName;

    @NotBlank(message = "{last.name.not.blank}")
    private String lastName;

    @Email(message = "{email.invalid.format}")
    @NotBlank(message = "{email.not.blank}")
    private String email;

    @NotBlank(message = "{password.not.blank}")
    private String password;

    @NotNull(message = "{date.of.birth.not.null}")
    private Date dateOfBirth;
//
//    @NotNull(message = "{active.status.not.null}")
//    private Boolean isActive;

    @NotNull(message = "{roles.not.null}")
    private List<Integer> roleIds;
}
