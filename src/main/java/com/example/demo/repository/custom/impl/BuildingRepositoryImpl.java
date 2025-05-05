package com.example.demo.repository.custom.impl;


import com.example.demo.builder.BuildingSearchBuilder;
import com.example.demo.entity.BuildingEntity;
import com.example.demo.repository.custom.BuildingRepositoryCustom;
import com.example.demo.utils.NumberUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Primary
public class BuildingRepositoryImpl implements BuildingRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;


    // xử lý các câu query => field cua chinh bang do (ngoai tru cac field tinh toan phuc tap)
    public static void queryNormal(BuildingSearchBuilder buildingSearchBuilder, StringBuilder where) {
        try {
            Field[] fields = BuildingSearchBuilder.class.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String fieldName = field.getName();
                if (!fieldName.startsWith("area")
                        && !fieldName.startsWith("sellingPrice")) {
                    Object value = field.get(buildingSearchBuilder);
                    if (value != null) {
                        // check là số hay chuỗi
                        if (field.getType().getName().equals("java.lang.Long")
                                || field.getType().getName().equals("java.lang.Integer")) {
                            where.append(" AND b." + fieldName + " = " + value);
                        } else if (field.getType().getName().equals("java.lang.String")) {
                            where.append(" AND b." + fieldName + " LIKE  '%" + value + "%' ");
                        }
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //  xử lý các câu query => field phai join voi cac bang khac va cac cau querry tinh toan phuc tap cua chinh no
    public static void querySpecial(BuildingSearchBuilder buildingSearchBuilder, StringBuilder where) {

        Double areaTo = buildingSearchBuilder.getAreaTo();
        Double areaFrom = buildingSearchBuilder.getAreaFrom();
        if (NumberUtils.checkNumberDouble(areaTo) || NumberUtils.checkNumberDouble(areaFrom)) {
            if (NumberUtils.checkNumberDouble(areaFrom)) {
                where.append(" AND b.total_area >= " + areaFrom);
            }
            if (NumberUtils.checkNumberDouble(areaTo)) {
                where.append(" AND b.total_area <= " + areaTo);
            }
        }

        Long sellingPrice = buildingSearchBuilder.getSellingPrice();
        if (NumberUtils.checkNumberLong(sellingPrice)) {
            where.append(" AND b.min_selling_price <= " + sellingPrice);
            where.append(" AND b.max_selling_price >= " + sellingPrice);

        }
    }

    @Override
    public List<BuildingEntity> searchBuildings(BuildingSearchBuilder buildingSearchBuilder) {
        StringBuilder sql = new StringBuilder("SELECT b.* FROM building b    ");
        StringBuilder where = new StringBuilder(" WHERE 1=1 ");

        queryNormal(buildingSearchBuilder, where);
        querySpecial(buildingSearchBuilder, where);

        where.append(" GROUP BY b.id ;");
        sql.append(where);

        Query query = entityManager.createNativeQuery(sql.toString(), BuildingEntity.class);
        return query.getResultList();
    }
    
    @Override
    public long countDistinctProjectName() {
    	String sql = """
                SELECT COUNT(*) FROM building;
            """;
            Query query = entityManager.createNativeQuery(sql);
            Object result = query.getSingleResult();
            return ((Number) result).longValue();
    }

    @Override
    public long countDistinctAdress() {
        String sql = """
            SELECT 
		    COUNT(DISTINCT
	        CASE 
	            WHEN address LIKE '%Q.%' THEN TRIM(SUBSTRING_INDEX(SUBSTRING_INDEX(address, 'Q.', -1), ',', 1))
	            ELSE NULL
	        END
		    ) AS district_or_county_count
			FROM building
			WHERE address IS NOT NULL;
        """;
        Query query = entityManager.createNativeQuery(sql);
        Object result = query.getSingleResult();
        return ((Number) result).longValue();
    }

	@Override
	public double countDistinctAverage() {
	    String sql = """
	            SELECT 
	                (SUM(min_selling_price) + SUM(max_selling_price)) / COUNT(*) AS average_price
	            FROM building
	        """;
	    Query query = entityManager.createNativeQuery(sql);
	    Object result = query.getSingleResult();
	    double value = ((Number) result).doubleValue();

	    double valueInBillion = value / 1_000_000_000.0;
	    return Math.round(valueInBillion * 10) / 10.0;
	}

	@Override
	public double avgPricePerM2() {
		String sql = """
				SELECT 
				    ROUND(
				        SUM(((min_selling_price + max_selling_price) / 2) / (total_area * 10000)) 
				        / 
				        COUNT(CASE 
				                WHEN min_selling_price IS NOT NULL 
				                AND max_selling_price IS NOT NULL 
				                AND total_area IS NOT NULL 
				                AND total_area != 0 
				                THEN 1 
				              END), 
				        2
				    ) AS total_avg_price_per_m2
				FROM building
				WHERE total_area != 0;
				""";
		Query query = entityManager.createNativeQuery(sql);
	    Object result = query.getSingleResult();
	    double value = ((Number) result).doubleValue();

	    double valueInBillion = value / 1_000.0;
	    return Math.round(valueInBillion * 10) / 10.0;
	}

	@Override
	public Map<String, Integer> getProjectCountByDistrict() {
	    Map<String, Integer> projectCountByDistrict = new HashMap<>();

	    String sql = """
	        SELECT 
		    CASE 
		        WHEN address LIKE '%Q.%' THEN TRIM(SUBSTRING_INDEX(SUBSTRING_INDEX(address, 'Q.', -1), ',', 1))
		        ELSE 'Unknown'
		    END AS district_or_county,
		    COUNT(*) AS project_count
			FROM building
			WHERE address IS NOT NULL
			GROUP BY 
			    CASE 
			        WHEN address LIKE '%Q.%' THEN TRIM(SUBSTRING_INDEX(SUBSTRING_INDEX(address, 'Q.', -1), ',', 1))
			        ELSE 'Unknown'
			    END
			ORDER BY district_or_county;
	    """;

	    Query query = entityManager.createNativeQuery(sql);
	    List<Object[]> results = query.getResultList();

	    for (Object[] row : results) {
	        String district = (String) row[0];
	        Integer projectCount = ((Number) row[1]).intValue();
	        projectCountByDistrict.put(district, projectCount);
	    }

	    return projectCountByDistrict;
	}	
	
	@Override
	public Map<String, Integer> getProjectCountByBikeParkingRange() {
	    Map<String, Integer> projectCountByRange = new HashMap<>();

	    String sql = """
	    	    SELECT 
	    	        bike_parking_category AS price_range,
	    	        COUNT(*) AS project_count
	    	    FROM (
	    	        SELECT 
	    	            CASE 
	    	                WHEN bike_parking_monthly IS NULL OR bike_parking_monthly <= 0 THEN 'No Fee'
	    	                WHEN bike_parking_monthly > 0 AND bike_parking_monthly <= 10000 THEN '0 - 10,000 VND'
	    	                WHEN bike_parking_monthly > 10000 AND bike_parking_monthly <= 50000 THEN '10,000 - 50,000 VND'
	    	                WHEN bike_parking_monthly > 50000 AND bike_parking_monthly <= 100000 THEN '50,000 - 100,000 VND'
	    	                WHEN bike_parking_monthly > 100000 AND bike_parking_monthly <= 500000 THEN '100,000 - 500,000 VND'
	    	                WHEN bike_parking_monthly > 500000 AND bike_parking_monthly <= 1000000 THEN '500,000 - 1 Million VND'
	    	                ELSE '> 1 Million VND'
	    	            END AS bike_parking_category
	    	        FROM building
	    	    ) subquery
	    	    GROUP BY bike_parking_category
	    	    ORDER BY 
	    	        CASE bike_parking_category
	    	            WHEN 'No Fee' THEN 1
	    	            WHEN '0 - 10,000 VND' THEN 2
	    	            WHEN '10,000 - 50,000 VND' THEN 3
	    	            WHEN '50,000 - 100,000 VND' THEN 4
	    	            WHEN '100,000 - 500,000 VND' THEN 5
	    	            WHEN '500,000 - 1 Million VND' THEN 6
	    	            ELSE 7
	    	        END
	    	""";

	    Query query = entityManager.createNativeQuery(sql);
	    List<Object[]> results = query.getResultList();

	    for (Object[] row : results) {
	        String priceRange = (String) row[0];
	        Integer projectCount = ((Number) row[1]).intValue();
	        projectCountByRange.put(priceRange, projectCount);
	    }

	    return projectCountByRange;
	}
	
	@Override
	public Map<String, Integer> getProjectCountByCarParkingRange() {
	    Map<String, Integer> projectCountByRange = new HashMap<>();

	    String sql = """
	    	    SELECT 
	    	        car_parking_category AS price_range,
	    	        COUNT(*) AS project_count
	    	    FROM (
	    	        SELECT 
	    	            CASE 
	    	                WHEN car_parking_monthly IS NULL OR car_parking_monthly <= 0 THEN 'No Fee'
	    	                WHEN car_parking_monthly > 0 AND car_parking_monthly <= 10000 THEN '0 - 10,000 VND'
	    	                WHEN car_parking_monthly > 10000 AND car_parking_monthly <= 50000 THEN '10,000 - 50,000 VND'
	    	                WHEN car_parking_monthly > 50000 AND car_parking_monthly <= 100000 THEN '50,000 - 100,000 VND'
	    	                WHEN car_parking_monthly > 100000 AND car_parking_monthly <= 500000 THEN '100,000 - 500,000 VND'
	    	                WHEN car_parking_monthly > 500000 AND car_parking_monthly <= 1000000 THEN '500,000 - 1 Million VND'
	    	                ELSE '> 1 Million VND'
	    	            END AS car_parking_category
	    	        FROM building
	    	    ) subquery
	    	    GROUP BY car_parking_category
	    	    ORDER BY 
	    	        CASE car_parking_category
	    	            WHEN 'No Fee' THEN 1
	    	            WHEN '0 - 10,000 VND' THEN 2
	    	            WHEN '10,000 - 50,000 VND' THEN 3
	    	            WHEN '50,000 - 100,000 VND' THEN 4
	    	            WHEN '100,000 - 500,000 VND' THEN 5
	    	            WHEN '500,000 - 1 Million VND' THEN 6
	    	            ELSE 7
	    	        END
	    	""";

	    Query query = entityManager.createNativeQuery(sql);
	    List<Object[]> results = query.getResultList();

	    for (Object[] row : results) {
	        String priceRange = (String) row[0];
	        Integer projectCount = ((Number) row[1]).intValue();
	        projectCountByRange.put(priceRange, projectCount);
	    }

	    return projectCountByRange;
	}
}

