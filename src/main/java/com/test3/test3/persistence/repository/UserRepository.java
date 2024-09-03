package com.test3.test3.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import com.test3.test3.persistence.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional <UserEntity> findUserEntityByUsername(String username);

    // Es lo mismo de lo de arriba
    // @Query("SELECT u FROM UserEntity u WHERE username = ?")
    // Optional<UserEntity> findUser(String username);

}
