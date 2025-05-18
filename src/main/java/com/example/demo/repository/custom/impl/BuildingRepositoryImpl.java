package com.example.demo.repository.custom.impl;

import com.example.demo.builder.BuildingSearchBuilder;
import com.example.demo.entity.BuildingEntity;
import com.example.demo.repository.custom.BuildingRepositoryCustom;
import com.example.demo.utils.NumberUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.Field;
import java.util.List;

@Repository
@Primary
public class BuildingRepositoryImpl implements BuildingRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    // map tên field Java với tên cột SQL tương ứng
    private static String camelToSnake(String str) {
        return str.replaceAll("([a-z])([A-Z]+)", "$1_$2").toLowerCase();
    }


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
                            where.append(" AND b." + camelToSnake(fieldName) + " = " + value);
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


}

