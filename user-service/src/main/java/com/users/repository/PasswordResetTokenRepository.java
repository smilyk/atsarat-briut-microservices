package com.users.repository;

import com.users.entity.resetPassword.PasswordResetTokenEntity;
import org.springframework.data.repository.CrudRepository;



public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetTokenEntity, Long>{
	PasswordResetTokenEntity findByToken(String token);
}
