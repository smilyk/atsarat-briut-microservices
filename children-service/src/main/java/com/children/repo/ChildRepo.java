package com.children.repo;

import com.children.entity.ChildrenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChildRepo extends JpaRepository<ChildrenEntity, Long> {

    Optional<ChildrenEntity> findByTzAndDeleted(String tz, boolean deleted);

    Optional<ChildrenEntity> findByUuidChildAndDeleted(String uuidChild, boolean b);
}
