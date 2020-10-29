package com.email.dto;



import com.email.enums.Services;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EmailDto {

    String childFirstName;
    String childSecondName;
    String email;
    String picture;
    Services service;
    String lastName ;
    String firstName ;
}
