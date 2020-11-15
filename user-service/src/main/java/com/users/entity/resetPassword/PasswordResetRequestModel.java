package com.users.entity.resetPassword;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PasswordResetRequestModel {
	private String email;
}
