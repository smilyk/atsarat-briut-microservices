package com.scheduler.services.hystrix;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginUserHystrixDto {

    String password;
    String mainEmail;
}

