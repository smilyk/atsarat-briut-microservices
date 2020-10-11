package com.scheduler.repository;

import com.scheduler.entity.PlanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StartTimeRepo extends JpaRepository<PlanEntity, Long> {


}
