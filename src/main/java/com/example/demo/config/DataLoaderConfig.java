package com.example.demo.config;

import com.example.demo.entity.BuildingEntity;
import com.example.demo.repository.BuildingRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;
import java.util.List;

// Khi chạy ứng dụng Spring Boot, hàm loadData() sẽ được gọi, đọc file JSON, và lưu toàn bộ dữ liệu vào database.
@Configuration
public class DataLoaderConfig {

    @Bean
    CommandLineRunner loadData(BuildingRepository buildingRepository) {
        return args -> {
            // Khởi tạo ObjectMapper và cấu hình chuyển snake_case -> camelCase
            ObjectMapper mapper = new ObjectMapper();
            mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

            // Đường dẫn tới file JSON trong resources/data
            InputStream inputStream = getClass().getResourceAsStream("/data/db_building_entity.json");

            try {
                // Đọc dữ liệu JSON thành danh sách BuildingEntity
                List<BuildingEntity> buildings = mapper.readValue(inputStream, new TypeReference<>() {});

                // Lưu tất cả vào database
                buildingRepository.saveAll(buildings);

                System.out.println("✅ Dữ liệu đã được import vào database thành công!");
            } catch (Exception e) {
                System.err.println("❌ Lỗi khi nạp dữ liệu: " + e.getMessage());
            }
        };
    }
}