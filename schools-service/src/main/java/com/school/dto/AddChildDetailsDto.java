package com.school.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class AddChildDetailsDto {
    @NotNull(message = "uuidChild can not be null")
    String uuidChild;
    @NotNull(message = "schoolUserName can not be null")
    String schoolUserName;
    @NotNull(message = "schoolPassword can not be null")
    private String schoolPassword;
}
