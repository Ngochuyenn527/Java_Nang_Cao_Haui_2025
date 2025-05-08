package com.example.demo.service.impl;

import com.example.demo.converter.ApartmentConverter;
import com.example.demo.entity.ApartmentEntity;
import com.example.demo.model.dto.ApartmentDTO;
import com.example.demo.repository.ApartmentRepository;
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

    // Kiểm tra xem căn hộ có tồn tại không
    private ApartmentEntity checkApartmentById(Long id) {
        return apartmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy căn hộ với ID: " + id));
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

    @Override
    @Transactional
    public ApartmentDTO addApartment(ApartmentDTO apartmentDTO) {
        ApartmentEntity apartmentEntity = apartmentConverter.convertToApartmentEntity(apartmentDTO);
        ApartmentEntity savedEntity = apartmentRepository.save(apartmentEntity);
        return apartmentConverter.convertToApartmentDto(savedEntity);
    }

    @Override
    @Transactional
    public ApartmentDTO updateApartment(Long id, ApartmentDTO apartmentDTO) {
        checkApartmentById(id); // Đảm bảo apartment tồn tại
        ApartmentEntity updatedEntity = apartmentConverter.convertToApartmentEntity(apartmentDTO);
        updatedEntity.setId(id); // Gán ID để Hibernate nhận biết là update
        ApartmentEntity saved = apartmentRepository.save(updatedEntity);
        return apartmentConverter.convertToApartmentDto(saved);
    }

    @Override
    @Transactional
    public void deleteApartment(Long id) {
        checkApartmentById(id); // Kiểm tra tồn tại
        apartmentRepository.deleteById(id);
    }
}
