package com.users.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode
public class UserProfileDto {
    private String uuidUser;
    private String secondName;
    private String altEmail;
    private String tz;
}
