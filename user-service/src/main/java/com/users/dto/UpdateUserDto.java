package com.users.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class UpdateUserDto {
    private String uuidUser;
    private String firstName;
    private String secondName;
    @Email(message = "you should put valid e-mail")
    private String altEmail;
    @Size(min=9, max=9, message = " tz should contains 9 numbers")
    private String tz;
}
