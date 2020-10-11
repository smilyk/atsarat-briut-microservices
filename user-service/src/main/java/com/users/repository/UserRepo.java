package com.users.repository;

import com.users.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepo extends JpaRepository<Users, Long> {

    Optional<Users> findByMainEmail(String eMail);

    Optional<Users> findUserByConfirmEmailToken(String token);

    Optional<Users> findByUuidUserAndDeleted(String uuid_user, boolean deleted);

}
