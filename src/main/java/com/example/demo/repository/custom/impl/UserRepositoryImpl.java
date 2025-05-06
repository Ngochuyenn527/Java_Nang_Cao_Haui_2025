package com.example.demo.repository.custom.impl;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.custom.UserRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserEntity> findByRoleCode(String roleCode) {
        String jpql = "SELECT u FROM UserEntity u WHERE u.role.code = :roleCode AND u.status = 1";
        TypedQuery<UserEntity> query = entityManager.createQuery(jpql, UserEntity.class);
        query.setParameter("roleCode", roleCode);
        return query.getResultList();
    }

    @Override
    public List<UserEntity> getAllUsers() {
        String sql = "SELECT * FROM user u WHERE u.status = 1";
        Query query = entityManager.createNativeQuery(sql.toString(), UserEntity.class);
        return query.getResultList();
    }


    @Override
    @Transactional
    public void updatePasswordById(Long id, String encodedPassword) {
        String sql = "UPDATE user SET password = ? WHERE id = ?";
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter(1, encodedPassword); // 1: vị trí đầu tiên
        query.setParameter(2, id);              // 2: vị trí thứ hai
        query.executeUpdate();
    }


}
