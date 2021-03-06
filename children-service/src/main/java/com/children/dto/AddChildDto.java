package com.children.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class AddChildDto {


    @NotNull(message = "first name can not be null")
    @Size(min = 2, max = 50)
    String firstName;
    @NotNull(message = "first name can not be null")
    @Size(min = 2, max = 50)
    String secondName;
    //    TODO to code
    @NotNull(message = "tz can not be null")
    @Size(min = 9, max = 9)
    private String tz;

    String uuidParent;
    String uuidRespPers;
}
