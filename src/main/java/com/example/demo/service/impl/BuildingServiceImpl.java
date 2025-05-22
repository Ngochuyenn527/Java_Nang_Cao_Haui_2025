package com.example.demo.service.impl;

import com.example.demo.builder.BuildingSearchBuilder;
import com.example.demo.converter.BuildingConverter;
import com.example.demo.converter.BuildingSearchBuilderConverter;
import com.example.demo.entity.BuildingEntity;
import com.example.demo.entity.SectorEntity;
import com.example.demo.model.dto.BuildingDTO;
import com.example.demo.model.request.BuildingSearchRequest;
import com.example.demo.model.response.BuildingSearchResponse;
import com.example.demo.repository.BuildingRepository;
import com.example.demo.repository.SectorRepository;
import com.example.demo.service.BuildingService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

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

    @Autowired
    private SectorRepository sectorRepository;

    // ✅ Kiểm tra tòa nhà theo ID có tồn tại không, nếu không có thì ném ngoại lệ
    public BuildingEntity checkBuildingById(Long id) {
        BuildingEntity existingBuilding = buildingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tòa nhà không tồn tại với ID: " + id));
        return existingBuilding;
    }

    // ✅ Kiểm tra Sector theo ID có tồn tại không (sử dụng cho add, update Building khi thêm sector_id), nếu không có thì ném ngoại lệ
    public SectorEntity checkSectorById(Long id) {
        SectorEntity existingSector = sectorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tồn tại phân khu với ID: " + id));
        return existingSector;
    }

    @Override
    public List<BuildingDTO> getAllBuildings() {
        return buildingRepository.findAll().stream()
                .map(buildingConverter::convertToBuildingDTO)
                .collect(Collectors.toList());
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

    /*
     Yêu cầu:
    Khi thêm Building, nếu có sector_id thì kiểm tra:

        + sector_id đó đã phải tồn tại.

        + SectorEntity.location phải chứa (contains) địa chỉ (BuildingDTO.address) (so sánh không phân biệt hoa thường).

    Nếu không thỏa, thì ném ngoại lệ kèm thông báo: "Địa chỉ tòa nhà không phù hợp với khu vực phân khu. Vui lòng kiểm tra lại sector_id."
    */
    @Override
    public BuildingDTO addBuilding(@Valid BuildingDTO buildingDTO) {
        try {
            // Nếu có sector, kiểm tra hợp lệ
            if (buildingDTO.getSector() != null && buildingDTO.getSector().getId() != null) {

                //check sector_id đó đã phải tồn tại.
                SectorEntity existingSector = checkSectorById(buildingDTO.getSector().getId());

                // ✅ So sánh location của sector phải chứa address của building (không phân biệt hoa thường)
                String buildingAddress = buildingDTO.getAddress() != null ? buildingDTO.getAddress().toLowerCase() : "";
                String sectorLocation = existingSector.getLocation() != null ? existingSector.getLocation().toLowerCase() : "";

                if (!buildingAddress.contains(sectorLocation)) {
                    throw new RuntimeException("Địa chỉ tòa nhà không phù hợp với khu vực phân khu. Vui lòng kiểm tra lại sector_id.");
                }
            }

            // ✅ Chuyển DTO -> Entity
            BuildingEntity buildingEntity = buildingConverter.convertToBuildingEntity(buildingDTO);

            // ✅ Lưu Building
            buildingRepository.save(buildingEntity);

            return buildingDTO;

        } catch (Exception e) {
            throw new RuntimeException("Có lỗi xảy ra khi thêm tòa nhà: " + e.getMessage());
        }
    }


    @Override
    public BuildingDTO updateBuilding(@Valid Long id, BuildingDTO buildingDTO) {
        try {
            BuildingEntity existingBuilding = checkBuildingById(id);

            // ✅ Kiểm tra sector nếu có
            if (buildingDTO.getSector() != null && buildingDTO.getSector().getId() != null) {

                //check sector_id đó đã phải tồn tại.
                SectorEntity existingSector = checkSectorById(buildingDTO.getSector().getId());

                // ✅ So sánh location và address (không phân biệt hoa thường)
                String buildingAddress = buildingDTO.getAddress() != null ? buildingDTO.getAddress().toLowerCase() : "";
                String sectorLocation = existingSector.getLocation() != null ? existingSector.getLocation().toLowerCase() : "";

                if (!buildingAddress.contains(sectorLocation)) {
                    throw new RuntimeException("Địa chỉ tòa nhà không phù hợp với khu vực phân khu. Vui lòng kiểm tra lại sector_id.");
                }
            }

            // ✅ Chuyển DTO -> Entity và giữ nguyên ID
            BuildingEntity updatedBuildingEntity = buildingConverter.convertToBuildingEntity(buildingDTO);
            updatedBuildingEntity.setId(id);

            buildingRepository.save(updatedBuildingEntity);

            return buildingDTO;

        } catch (RuntimeException e) {
            throw new RuntimeException("Có lỗi xảy ra khi cập nhật tòa nhà: " + e.getMessage());
        }
    }


    @Override
    public void deleteBuilding(Long id) {
        try {
            checkBuildingById(id);// Kiểm tra tồn tại
            buildingRepository.deleteById(id); // Xóa

        } catch (RuntimeException e) {
            throw new RuntimeException("Có lỗi xảy ra khi xóa tòa nhà: " + e.getMessage());
        }
    }
    
    @Override
    public long getDistinctProjectCount() {
    	return buildingRepository.countDistinctProjectName();
    }

	@Override
	public long getDistinctAddress() {
		return buildingRepository.countDistinctAdress();
	}

	@Override
	public double getDistinctAverage() {
		return buildingRepository.countDistinctAverage();
	}

	@Override
	public ObservableList<PieChart.Data> getPieChartDataForProjectsByDistrict() {
	    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
	    var projectCountByDistrict = buildingRepository.getProjectCountByDistrict();
	    for (var entry : projectCountByDistrict.entrySet()) {
	        String district = entry.getKey();
	        Integer count = entry.getValue();
	        pieChartData.add(new PieChart.Data(district, count));
	    }

	    return pieChartData;
	}
	
	@Override
	public List<XYChart.Series<String, Number>> getBarChartDataForProjectsByParkingFees() {
	    // Tạo hai series: một cho xe máy, một cho ô tô
	    XYChart.Series<String, Number> bikeSeries = new XYChart.Series<>();
	    bikeSeries.setName("Phí đỗ xe máy");

	    XYChart.Series<String, Number> carSeries = new XYChart.Series<>();
	    carSeries.setName("Phí đỗ ô tô");

	    // Lấy dữ liệu từ Repository
	    var bikeParkingData = buildingRepository.getProjectCountByBikeParkingRange();
	    var carParkingData = buildingRepository.getProjectCountByCarParkingRange();

	    // Tạo tập hợp tất cả các khoảng giá (hợp của cả xe máy và ô tô)
	    Set<String> allRanges = new TreeSet<>();
	    allRanges.addAll(bikeParkingData.keySet());
	    allRanges.addAll(carParkingData.keySet());

	    // Thêm dữ liệu vào series
	    for (String range : allRanges) {
	        // Dữ liệu cho xe máy
	        Integer bikeCount = bikeParkingData.getOrDefault(range, 0);
	        bikeSeries.getData().add(new XYChart.Data<>(range, bikeCount));

	        // Dữ liệu cho ô tô
	        Integer carCount = carParkingData.getOrDefault(range, 0);
	        carSeries.getData().add(new XYChart.Data<>(range, carCount));
	    }

	    return List.of(bikeSeries, carSeries);
	}

}
