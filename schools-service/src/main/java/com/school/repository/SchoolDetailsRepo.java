package com.school.repository;

import com.school.entity.SchoolDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolDetailsRepo extends JpaRepository<SchoolDetails, Long> {

    Optional<SchoolDetails> findByUuidChildAndDeleted(String uuidChild, boolean deleted);

    Optional<SchoolDetails> findByUuidChildDetailsAndDeleted(String uuidChildDetails, boolean deleted);
}
