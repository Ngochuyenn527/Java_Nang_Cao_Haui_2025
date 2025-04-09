package com.example.demo.converter;

import com.example.demo.builder.BuildingSearchBuilder;
import com.example.demo.model.request.BuildingSearchRequest;
import com.example.demo.utils.MapUtils;
import org.springframework.stereotype.Component;

@Component
// Chuyển dữ liệu từ BuildingSearchRequest sang BuildingSearchBuilder để phục vụ tìm kiếm linh hoạt
public class BuildingSearchBuilderConverter {
    public BuildingSearchBuilder toBuildingSearchBuilder(BuildingSearchRequest request) {
        return new BuildingSearchBuilder.Builder()
                .setName(MapUtils.getObjects(request.getName(), String.class))
                .setAddress(MapUtils.getObjects(request.getAddress(), String.class))
                .setRank(MapUtils.getObjects(request.getRank(), String.class))
                .setAreaFrom(MapUtils.getObjects(request.getAreaFrom(), Double.class))
                .setAreaTo(MapUtils.getObjects(request.getAreaTo(), Double.class))
                .setSellingPrice(MapUtils.getObjects(request.getSellingPrice(), Double.class))
                .setNumberLivingFloor(MapUtils.getObjects(request.getNumberLivingFloor(), Integer.class))
                .setNumberBasement(MapUtils.getObjects(request.getNumberBasement(), Integer.class))
                .build();
    }
}

