package com.users.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class RespPersonResponseDto {
    String uuidRespPerson;
    String firstName;
    String secondName;
}
