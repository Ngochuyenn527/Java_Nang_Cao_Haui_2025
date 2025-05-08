package com.example.demo.repository.custom.impl;

import com.example.demo.entity.SectorEntity;
import com.example.demo.repository.custom.SectorRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class SectorRepositoryImpl implements SectorRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<SectorEntity> getByLocation(String location) {
        String jpql = "SELECT s FROM SectorEntity s WHERE s.location LIKE :location";
        TypedQuery<SectorEntity> query = entityManager.createQuery(jpql, SectorEntity.class);
        query.setParameter("location", "%" + location + "%");
        return query.getResultList();
    }

    @Override
    public List<SectorEntity> getByStatus(String status) {
        String jpql = "SELECT s FROM SectorEntity s WHERE s.status = :status";
        TypedQuery<SectorEntity> query = entityManager.createQuery(jpql, SectorEntity.class);
        query.setParameter("status", status);
        return query.getResultList();
    }
}
