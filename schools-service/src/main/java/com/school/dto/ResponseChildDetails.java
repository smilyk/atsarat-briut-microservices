package com.school.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ResponseChildDetails {
    @NotNull(message = "uuidChildDetails can not be null")
    String uuidChildDetails;
    @NotNull(message = "uuidChild can not be null")
    String uuidChild;
    @NotNull(message = "schoolUserName can not be null")
    String schoolUserName;
}
