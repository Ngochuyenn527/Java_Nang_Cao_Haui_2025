package com.example.demo.repository.custom.impl;

import com.example.demo.entity.ApartmentEntity;
import com.example.demo.repository.custom.ApartmentRepositoryCustom;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ApartmentRepositoryImpl implements ApartmentRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<ApartmentEntity> getByStatus(String status) {
        String jpql = "SELECT a FROM ApartmentEntity a WHERE a.status = :status";
        TypedQuery<ApartmentEntity> query = entityManager.createQuery(jpql, ApartmentEntity.class);
        query.setParameter("status", status);
        return query.getResultList();
    }

    @Override
    public List<ApartmentEntity> getByFloor(Integer floor) {
        String jpql = "SELECT a FROM ApartmentEntity a WHERE a.floor = :floor";
        TypedQuery<ApartmentEntity> query = entityManager.createQuery(jpql, ApartmentEntity.class);
        query.setParameter("floor", floor);
        return query.getResultList();
    }
}
