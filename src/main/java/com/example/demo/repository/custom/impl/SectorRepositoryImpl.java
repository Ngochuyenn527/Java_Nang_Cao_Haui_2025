package com.example.demo.repository.custom.impl;

import com.example.demo.entity.SectorEntity;
import com.example.demo.repository.custom.SectorRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Override
	public long countSector() {
		String sql = """
                SELECT COUNT(*) FROM sector;
            """;
        Query query = entityManager.createNativeQuery(sql);
        Object result = query.getSingleResult();
        return ((Number) result).longValue();
	}

	@Override
	public Map<Integer, Long> getSectorCountByYear() {
		Map<Integer, Long> sectorCountByYear = new HashMap<>();
        String sql = "SELECT YEAR(start_date) AS year, COUNT(*) AS sector_count " +
                     "FROM sector " +
                     "WHERE start_date IS NOT NULL " +
                     "GROUP BY YEAR(start_date) " +
                     "ORDER BY year";

        Query query = entityManager.createNativeQuery(sql);
        List<Object[]> results = query.getResultList();

        for (Object[] row : results) {
            Integer year = ((Number) row[0]).intValue(); // Ép kiểu year từ Number sang Integer
            Long sectorCount = ((Number) row[1]).longValue(); // Ép kiểu sector_count từ Number sang Long
            sectorCountByYear.put(year, sectorCount);
        }

        return sectorCountByYear;
    }
}
