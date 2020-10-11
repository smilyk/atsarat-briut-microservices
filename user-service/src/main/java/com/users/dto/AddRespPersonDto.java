package com.users.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class AddRespPersonDto {
    @NotNull(message = "e-mail can not be null")
    @Email(message = "you should put valid e-mail")
    String emailRespPerson;
    @NotNull(message = "first name can not be null")
    @Size(min=2, max=50)
    String firstName;
    @NotNull(message = "second name can not be null")
    @Size(min=2, max=50)
    String secondName;
    @NotNull(message = "teudat zeut can not be null")
    @Size(min=9, max=9, message = " tz should contains 9 numbers")
    String tzRespPers;
}
