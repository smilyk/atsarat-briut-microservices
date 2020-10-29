package com.tsofim.servicers.hystrix.user.parent;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserHystrixDto {

    String uuidUser;
    String mainEmail;
    String firstName;
    String secondName;
    String altEmail;
    String tz;

}

