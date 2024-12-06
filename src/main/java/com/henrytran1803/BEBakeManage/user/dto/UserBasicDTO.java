package com.henrytran1803.BEBakeManage.user.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserBasicDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

}
