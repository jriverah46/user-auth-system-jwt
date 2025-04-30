package com.jr.security_no_guide.persistence.repository;

import com.jr.security_no_guide.persistence.entity.RoleEntity;
import com.jr.security_no_guide.persistence.entity.RoleEnum;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends ListCrudRepository<RoleEntity,Long> {
    Optional<RoleEntity> findByRoleName(RoleEnum roleName);
}
