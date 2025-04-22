package com.example.demo.repository;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.custom.UserRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> , UserRepositoryCustom {
    UserEntity findOneByUserNameAndStatus(String name, int status);
    List<UserEntity> findByStatusAndRoles_Code(Integer status, String roleCode);
//    UserEntity findOneByUserName(String userName);
}
