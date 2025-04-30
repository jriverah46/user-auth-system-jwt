package com.jr.security_no_guide.persistence.repository;

import com.jr.security_no_guide.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends ListCrudRepository <UserEntity,Long> {
    @Query("SELECT u FROM UserEntity u WHERE u.username=:username")
    Optional<UserEntity>findByUsername(@Param("username") String username);

    Boolean existsByUsername(String username);


}
