package com.example.demo.repository.custom;

import com.example.demo.entity.UserEntity;

import java.util.List;

//class tùy chỉnh (custom) cho repository, dùng để viết các câu SQL nâng cao (native SQL hoặc JPQL) mà Spring Data JPA mặc định không hỗ trợ.
public interface UserRepositoryCustom {
    List<UserEntity> getByRoleCode(String roleCode);

    List<UserEntity> getAllUsersHasStatus1();

    void updatePasswordById(Long id, String encodedPassword);

}
