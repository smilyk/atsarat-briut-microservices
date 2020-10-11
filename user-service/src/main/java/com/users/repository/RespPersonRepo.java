package com.users.repository;

import com.users.entity.ResponsePerson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RespPersonRepo extends JpaRepository<ResponsePerson, Long> {


    Optional<ResponsePerson> findByEmailRespPerson(String emailRespPerson);

    Optional<ResponsePerson> findByUuidRespPersonAndDeleted(String uuidRespPerson, boolean b);
}
