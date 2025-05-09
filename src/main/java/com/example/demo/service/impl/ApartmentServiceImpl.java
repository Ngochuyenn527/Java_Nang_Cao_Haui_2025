package com.example.demo.service.impl;

import com.example.demo.converter.ApartmentConverter;
import com.example.demo.entity.ApartmentEntity;
import com.example.demo.entity.BuildingEntity;
import com.example.demo.model.dto.ApartmentDTO;
import com.example.demo.repository.ApartmentRepository;
import com.example.demo.repository.BuildingRepository;
import com.example.demo.service.ApartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ApartmentServiceImpl implements ApartmentService {

    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private ApartmentConverter apartmentConverter;

    @Autowired
    private BuildingRepository buildingRepository;

    // Kiểm tra xem căn hộ có tồn tại không
    private ApartmentEntity checkApartmentById(Long id) {
        return apartmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy căn hộ với ID: " + id));
    }

    // Kiểm tra Building theo ID có tồn tại không (sử dụng cho add, update Apartment khi thêm building_id), nếu không có thì ném ngoại lệ
    public BuildingEntity checkBuildingById(Long id) {
        BuildingEntity existingBuilding = buildingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tòa nhà không tồn tại với ID: " + id));
        return existingBuilding;
    }

    @Override
    public List<ApartmentDTO> getAllApartments() {
        return apartmentRepository.findAll().stream()
                .map(apartmentConverter::convertToApartmentDto)
                .collect(Collectors.toList());
    }

    @Override
    public ApartmentDTO getApartmentById(Long id) {
        return apartmentConverter.convertToApartmentDto(checkApartmentById(id));
    }

    @Override
    public List<ApartmentDTO> getApartmentsByFloor(Integer floor) {
        return apartmentRepository.getByFloor(floor).stream()
                .map(apartmentConverter::convertToApartmentDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ApartmentDTO> getApartmentsByStatus(String status) {
        return apartmentRepository.getByStatus(status).stream()
                .map(apartmentConverter::convertToApartmentDto)
                .collect(Collectors.toList());
    }


    /*
     Yêu cầu:
    Khi thêm Apartment, nếu có building_id thì kiểm tra:

        + building_id đó đã phải tồn tại.

        +  "tên căn hộ (apartment.name) phải chứa tên tòa nhà (building.name) không phân biệt hoa thường"

    Nếu không thỏa, thì ném ngoại lệ kèm thông báo: "Tên căn hộ không phù hợp với tên tòa nhà nào. Vui lòng kiểm tra lại sector_id."
    */
    @Override
    public ApartmentDTO addApartment(ApartmentDTO apartmentDTO) {
        try {
            // Nếu có building, kiểm tra hợp lệ
            if (apartmentDTO.getBuilding() != null && apartmentDTO.getBuilding().getId() != null) {

                //check building_id đó đã phải tồn tại.
                BuildingEntity existingBuilding = checkBuildingById(apartmentDTO.getBuilding().getId());

                // So sánh "tên căn hộ (apartment.name) phải chứa tên tòa nhà (building.name)
                String apartmentName = apartmentDTO.getName() != null ? apartmentDTO.getName().toLowerCase() : "";
                String buildingName = existingBuilding.getName() != null ? existingBuilding.getName().toLowerCase() : "";

                if (!apartmentName.contains(buildingName)) {
                    throw new RuntimeException("Tên căn hộ không phù hợp với tên tòa nhà nào");
                }
            }

            // ✅ Chuyển DTO -> Entity
            ApartmentEntity apartmentEntity = apartmentConverter.convertToApartmentEntity(apartmentDTO);

            // ✅ Lưu Building
            apartmentRepository.save(apartmentEntity);

            return apartmentDTO;

        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra khi thêm căn hộ: " + e.getMessage());
        }
    }


    @Override
    public ApartmentDTO updateApartment(Long id, ApartmentDTO apartmentDTO) {
        try {
            ApartmentEntity existingApartment = checkApartmentById(id);

            // Nếu có building, kiểm tra hợp lệ
            if (apartmentDTO.getBuilding() != null && apartmentDTO.getBuilding().getId() != null) {

                //check building_id đó đã phải tồn tại.
                BuildingEntity existingBuilding = checkBuildingById(apartmentDTO.getBuilding().getId());

                // So sánh "tên căn hộ (apartment.name) phải chứa tên tòa nhà (building.name)
                String apartmentName = apartmentDTO.getName() != null ? apartmentDTO.getName().toLowerCase() : "";
                String buildingName = existingBuilding.getName() != null ? existingBuilding.getName().toLowerCase() : "";

                if (!apartmentName.contains(buildingName)) {
                    throw new RuntimeException("Tên căn hộ không phù hợp với tên tòa nhà nào");
                }
            }

            // ✅ Chuyển DTO -> Entity và giữ nguyên ID
            ApartmentEntity updatedApartmentEntity = apartmentConverter.convertToApartmentEntity(apartmentDTO);
            updatedApartmentEntity.setId(id);

            apartmentRepository.save(updatedApartmentEntity);

            return apartmentDTO;

        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra khi thêm căn hộ: " + e.getMessage());
        }
    }


    @Override
    @Transactional
    public void deleteApartment(Long id) {
        try {
            checkApartmentById(id); // Kiểm tra tồn tại
            apartmentRepository.deleteById(id); // Xóa

        } catch (RuntimeException e) {
            throw new RuntimeException("Có lỗi xảy ra khi xóa căn hộ: " + e.getMessage());
        }
    }
}
