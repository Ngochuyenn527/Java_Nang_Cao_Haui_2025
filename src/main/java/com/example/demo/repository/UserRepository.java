package com.example.demo.repository;

import com.example.demo.config.UserPrincipal;
import com.example.demo.config.NotFoundException;
import com.example.demo.entity.UserEntity;
import com.example.demo.repository.custom.UserRepositoryCustom;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, Long>, UserRepositoryCustom {
    UserEntity findOneByUserNameAndStatus(String name, int status);
    
    boolean existsByUserName(String userName);
    
    @Query("SELECT u FROM UserEntity u WHERE u.userName = ?1")
    Optional<UserEntity> findByUsername(String username);

    default UserEntity getUser(UserPrincipal currentUser) {
        return findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new NotFoundException(
                        "User not found with username: " + currentUser.getUsername()
                ));
    }
}
