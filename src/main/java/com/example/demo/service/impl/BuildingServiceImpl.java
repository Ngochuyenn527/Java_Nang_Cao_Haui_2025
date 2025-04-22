package com.example.demo.service.impl;

import com.example.demo.builder.BuildingSearchBuilder;
import com.example.demo.converter.BuildingConverter;
import com.example.demo.converter.BuildingSearchBuilderConverter;
import com.example.demo.entity.BuildingEntity;
import com.example.demo.model.dto.BuildingDTO;
import com.example.demo.model.request.BuildingSearchRequest;
import com.example.demo.model.response.BuildingSearchResponse;
import com.example.demo.repository.BuildingRepository;
import com.example.demo.service.BuildingService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class BuildingServiceImpl implements BuildingService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private BuildingSearchBuilderConverter buildingSearchBuilderConverter;

    @Autowired
    private BuildingConverter buildingConverter;

    // ✅ Kiểm tra tòa nhà theo ID có tồn tại không, nếu không có thì ném ngoại lệ
    public BuildingEntity checkBuildingById(Long id) {
        BuildingEntity existingBuilding = buildingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tòa nhà không tồn tại với ID: " + id));
        return existingBuilding;
    }

    @Override
    public List<BuildingSearchResponse> searchBuildings(BuildingSearchRequest buildingSearchRequest) {

        //Chuyển dữ liệu tìm kiếm BuildingSearchRequest thành BuildingSearchBuilder
        BuildingSearchBuilder buildingSearchBuilder = buildingSearchBuilderConverter.toBuildingSearchBuilder(buildingSearchRequest);

        //Gọi Repository để lấy danh sách tòa nhà
        List<BuildingEntity> buildingEntities = buildingRepository.searchBuildings(buildingSearchBuilder);

        List<BuildingSearchResponse> result = buildingEntities.stream()
                .map(entity -> modelMapper.map(entity, BuildingSearchResponse.class))
                .toList();
        return result;
    }

    @Override
    public BuildingDTO getBuildingById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Building ID must not be null!");
        }
        // Chuyển đổi Entity -> DTO
        return buildingConverter.convertToBuildingDTO(checkBuildingById(id));
    }


    @Override
    public BuildingDTO addBuilding(@Valid BuildingDTO buildingDTO) {

        try {
            // ✅ Chuyển từ DTO -> Entity
            BuildingEntity addbuildingEntity = buildingConverter.convertToBuildingEntity(buildingDTO);

            // ✅ Lưu lại tòa nhà (RentAreaEntities sẽ được lưu tự động nếu đã cấu hình cascade)
            buildingRepository.save(addbuildingEntity);

            return buildingDTO;

        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra khi thêm tòa nhà: " + e.getMessage());
        }
    }


    @Override
    public BuildingDTO updateBuilding(@Valid Long id, BuildingDTO buildingDTO) {
        try {
            BuildingEntity existingBuilding = checkBuildingById(id);

            // ✅ Chuyển từ DTO -> Entity (giữ nguyên ID)
            BuildingEntity updatedBuildingEntity = buildingConverter.convertToBuildingEntity(buildingDTO);
            updatedBuildingEntity.setId(id);

            // ✅ Lưu thông tin tòa nhà đã cập nhật (bao gồm cả RentAreaEntities nếu có cascade)
            buildingRepository.save(updatedBuildingEntity);

            return buildingDTO;

        } catch (RuntimeException e) {
            // ✅ Xử lý ngoại lệ nếu không tìm thấy tòa nhà hoặc có lỗi trong quá trình cập nhật
            throw new RuntimeException("Có lỗi xảy ra khi cập nhật tòa nhà: " + e.getMessage());
        }
    }

    @Override
    public void deleteBuilding(Long id) {
        try {
            BuildingEntity existingBuilding = checkBuildingById(id);

            // ✅ Xóa tòa nhà khỏi cơ sở dữ liệu
            buildingRepository.delete(existingBuilding);

        } catch (RuntimeException e) {
            // ✅ Xử lý ngoại lệ nếu không tìm thấy tòa nhà hoặc có lỗi trong quá trình xóa
            throw new RuntimeException("Có lỗi xảy ra khi xóa tòa nhà: " + e.getMessage());
        }
    }

}
