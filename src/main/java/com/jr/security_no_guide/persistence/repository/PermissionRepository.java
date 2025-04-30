package com.jr.security_no_guide.persistence.repository;

import com.jr.security_no_guide.persistence.entity.PermissionEntity;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends ListCrudRepository<PermissionEntity,Long> {
}
