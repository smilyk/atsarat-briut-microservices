package com.users.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class RepsoncePersonDto {
    String uuidRespPerson;
    String emailRespPerson;
    String firstName;
    String secondName;
    String tzRespPers;
}
