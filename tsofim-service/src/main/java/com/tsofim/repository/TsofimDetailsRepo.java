package com.tsofim.repository;

import com.tsofim.entity.TsofimDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TsofimDetailsRepo extends JpaRepository<TsofimDetails, Long> {

    Optional<TsofimDetails> findByUuidChildAndDeleted(String uuidChild, boolean deleted);

    Optional<TsofimDetails> findByUuidTsofimDetailsAndDeleted(String uuidTsofimDetails, boolean b);
}
