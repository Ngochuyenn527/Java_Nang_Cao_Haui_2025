//package com.example.demo.config;
//
//import com.example.demo.entity.ApartmentEntity;
//import com.example.demo.entity.BuildingEntity;
//import com.example.demo.entity.SectorEntity;
//import com.example.demo.repository.ApartmentRepository;
//import com.example.demo.repository.BuildingRepository;
//import com.example.demo.repository.SectorRepository;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.InputStream;
//import java.util.List;
//
//@Configuration
//public class DataLoaderConfig {
//
//    @Autowired
//    private ObjectMapper objectMapper;  // Inject ObjectMapper
//
//    @Bean
//    public CommandLineRunner loadData(BuildingRepository buildingRepository, SectorRepository sectorRepository, ApartmentRepository apartmentRepository) {
//        return args -> {
//            try {
//
//                // Đọc danh sách SectorEntity từ JSON
//                InputStream sectorStream = getClass().getResourceAsStream("/data/db_sector_entity.json");
//                if (sectorStream == null) {
//                    System.err.println("❌ Không tìm thấy file JSON cho SectorEntity!");
//                    return;
//                }
//                List<SectorEntity> sectors = objectMapper.readValue(sectorStream, new TypeReference<List<SectorEntity>>() {});
//                sectorRepository.saveAll(sectors);
//
//
//                // Đọc danh sách BuildingEntity từ JSON
//                InputStream buildingStream = getClass().getResourceAsStream("/data/db_building_entity_with_sectorId.json");
//                if (buildingStream == null) {
//                    System.err.println("❌ Không tìm thấy file JSON cho BuildingEntity!");
//                    return;
//                }
//                List<BuildingEntity> buildings = objectMapper.readValue(buildingStream, new TypeReference<List<BuildingEntity>>() {});
//                buildingRepository.saveAll(buildings);
//
//
//                // Đọc danh sách ApartmentEntity từ JSON
//                InputStream aptStream = getClass().getResourceAsStream("/data/all_apartments_with_building.json");
//                if (aptStream == null) {
//                    System.err.println("❌ Không tìm thấy file JSON cho ApartmentEntity!");
//                    return;
//                }
//                List<ApartmentEntity> apts = objectMapper.readValue(aptStream, new TypeReference<List<ApartmentEntity>>() {});
//                apartmentRepository.saveAll(apts);
//
//                System.out.println("✅ Dữ liệu Building & Sector & ApartmentEntity đã được import thành công!");
//
//            } catch (Exception e) {
//                System.err.println("❌ Lỗi khi nạp dữ liệu: " + e.getMessage());
//            }
//        };
//    }
//}
