package com.example.demo.repository.custom.impl;

import com.example.demo.entity.UserEntity;
import com.example.demo.repository.custom.UserRepositoryCustom;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserEntity> getByRoleCode(String roleCode) {
        String jpql = "SELECT u FROM UserEntity u WHERE u.role.code = :roleCode AND u.status = 1";
        TypedQuery<UserEntity> query = entityManager.createQuery(jpql, UserEntity.class);
        query.setParameter("roleCode", roleCode);
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
