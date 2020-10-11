package com.scheduler.repository;

import com.scheduler.entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlanRepo extends JpaRepository<PlanEntity, Long> {

    Optional<PlanEntity> findByUuidPlanAndDeleted(String uuidPlanDetails, boolean deleted);

    Optional<PlanEntity> findByUuidChildAndDeleted(String uuidChild, boolean deleted);

}