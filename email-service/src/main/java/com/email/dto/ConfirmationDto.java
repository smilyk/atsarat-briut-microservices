package com.email.dto;



import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ConfirmationDto {
    String userName;
    String userLastName;
    String childFirstName;
    String childSecondName;
    String email;
    String picture;


}
