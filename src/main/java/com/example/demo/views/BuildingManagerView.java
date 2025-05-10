package com.example.demo.views;

import com.example.demo.model.dto.BuildingDTO;
import com.example.demo.model.dto.SectorDTO;
import com.example.demo.views.CommonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.layout.Pane;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class BuildingManagerView {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Long selectedBuildingId;

    public BuildingManagerView() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    public void setUpTableColumns(TableView<BuildingDTO> buildingList, TableColumn<BuildingDTO, Long> sectorId,
                                 TableColumn<BuildingDTO, String> code, TableColumn<BuildingDTO, String> name,
                                 TableColumn<BuildingDTO, String> address, TableColumn<BuildingDTO, String> developerName,
                                 TableColumn<BuildingDTO, String> rank, TableColumn<BuildingDTO, Double> totalArea,
                                 TableColumn<BuildingDTO, Long> minSellingPrice, TableColumn<BuildingDTO, Long> maxSellingPrice,
                                 TableColumn<BuildingDTO, Integer> numberEle, TableColumn<BuildingDTO, Integer> numberLivingFloor,
                                 TableColumn<BuildingDTO, Integer> numberBasement, TableColumn<BuildingDTO, Long> bikeParkingMonthly,
                                 TableColumn<BuildingDTO, Long> carParkingMonthly) {
        sectorId.setCellValueFactory(cellData -> {
            SectorDTO sector = cellData.getValue().getSector();
            Long sectorIdValue = (sector != null) ? sector.getId() : null;
            return new ReadOnlyObjectWrapper<>(sectorIdValue);
        });

        code.setCellValueFactory(new PropertyValueFactory<>("code"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        developerName.setCellValueFactory(new PropertyValueFactory<>("developerName"));
        rank.setCellValueFactory(new PropertyValueFactory<>("rank"));
        totalArea.setCellValueFactory(new PropertyValueFactory<>("totalArea"));
        minSellingPrice.setCellValueFactory(new PropertyValueFactory<>("minSellingPrice"));
        maxSellingPrice.setCellValueFactory(new PropertyValueFactory<>("maxSellingPrice"));
        numberEle.setCellValueFactory(new PropertyValueFactory<>("numberEle"));
        numberLivingFloor.setCellValueFactory(new PropertyValueFactory<>("numberLivingFloor"));
        numberBasement.setCellValueFactory(new PropertyValueFactory<>("numberBasement"));
        bikeParkingMonthly.setCellValueFactory(new PropertyValueFactory<>("bikeParkingMonthly"));
        carParkingMonthly.setCellValueFactory(new PropertyValueFactory<>("carParkingMonthly"));
    }

    public void fetchDataFromApi(TableView<BuildingDTO> buildingList) {
        try {
            String url = "http://localhost:8081/api/building";
            HttpHeaders headers = CommonUtils.createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<BuildingDTO> buildings = objectMapper.readValue(
                        response.getBody(),
                        new TypeReference<>() {}
                );
                buildingList.setItems(FXCollections.observableArrayList(buildings));
                System.out.println("Đã tải " + buildings.size() + " tòa nhà.");
            } else {
                CommonUtils.showError("Không thể lấy dữ liệu: " + response.getStatusCode());
            }
        } catch (Exception e) {
            CommonUtils.showError("Lỗi khi gọi API: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void handleSearch(TableView<BuildingDTO> buildingList, TextField txtname, TextField txtrank,
                             TextField txtward, TextField txtdistrict, TextField txtareaFrom, TextField txtareaTo,
                             TextField txtsellingPrice, TextField txtnumberLivingFloor, TextField txtnumberBasement) {
        String query = buildSearchQuery(txtname, txtrank, txtward, txtdistrict, txtareaFrom, txtareaTo,
                txtsellingPrice, txtnumberLivingFloor, txtnumberBasement);
        if (query.isEmpty()) return;

        try {
            String url = "http://localhost:8081/api/building/search?" + query;
            HttpHeaders headers = CommonUtils.createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<BuildingDTO> buildings = objectMapper.readValue(
                        response.getBody(),
                        new TypeReference<>() {}
                );
                buildingList.setItems(FXCollections.observableArrayList(buildings));
            } else {
                CommonUtils.showError("Không thể tìm kiếm dữ liệu: " + response.getStatusCode());
            }
        } catch (Exception e) {
            CommonUtils.showError("Lỗi khi gọi API tìm kiếm: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String buildSearchQuery(TextField txtname, TextField txtrank, TextField txtward, TextField txtdistrict,
                                   TextField txtareaFrom, TextField txtareaTo, TextField txtsellingPrice,
                                   TextField txtnumberLivingFloor, TextField txtnumberBasement) {
        StringBuilder query = new StringBuilder();

        appendQueryParam(query, "name", txtname);
        appendQueryParam(query, "rank", txtrank);

        String address = buildAddress(txtward, txtdistrict);
        if (!address.isBlank()) {
            query.append("&address=").append(address);
        }

        appendNumericQueryParam(query, "areaFrom", txtareaFrom);
        appendNumericQueryParam(query, "areaTo", txtareaTo);
        appendNumericQueryParam(query, "sellingPrice", txtsellingPrice);
        appendNumericQueryParam(query, "numberLivingFloor", txtnumberLivingFloor);
        appendNumericQueryParam(query, "numberBasement", txtnumberBasement);

        return query.toString();
    }

    private String buildAddress(TextField txtward, TextField txtdistrict) {
        String ward = txtward.getText().trim();
        String district = txtdistrict.getText().trim();

        if (!ward.isEmpty() && !district.isEmpty()) {
            return String.format("P. %s, Q. %s", ward, district);
        }
        if (!ward.isEmpty()) {
            return "P. " + ward;
        }
        if (!district.isEmpty()) {
            return "Q. " + district;
        }
        return "";
    }

    private void appendQueryParam(StringBuilder query, String param, TextField textField) {
        if (!textField.getText().isEmpty()) {
            query.append(param).append("=").append(textField.getText()).append("&");
        }
    }

    private void appendNumericQueryParam(StringBuilder query, String param, TextField textField) {
        if (!textField.getText().isEmpty()) {
            try {
                Double.parseDouble(textField.getText());
                query.append(param).append("=").append(textField.getText()).append("&");
            } catch (NumberFormatException e) {
                CommonUtils.showError(param + " phải là số hợp lệ!");
            }
        }
    }

    public void handleDeleteBuilding(TableView<BuildingDTO> buildingList) {
        BuildingDTO selected = buildingList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            CommonUtils.showError("Chưa chọn tòa nhà nào để xóa!");
            return;
        }

        confirmDelete(selected.getId(), buildingList);
    }

    private void confirmDelete(Long buildingId, TableView<BuildingDTO> buildingList) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa tòa nhà này không?");
        confirmAlert.setContentText("Thao tác này không thể hoàn tác.");

        confirmAlert.showAndWait().ifPresent(result -> {
        	if (result.getText().equals("OK")) {
        	    deleteBuilding(buildingId, buildingList);
        	    loadBuildingList(buildingList);
        	}
        });
    }

    private void deleteBuilding(Long buildingId, TableView<BuildingDTO> buildingList) {
        String url = "http://localhost:8081/api/building/" + buildingId;
        try {
            HttpHeaders headers = CommonUtils.createAuthHeaders();
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                CommonUtils.showSuccess("Đã xóa tòa nhà thành công!");
                fetchDataFromApi(buildingList);
            } else {
                CommonUtils.handleApiError(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            CommonUtils.showError("Lỗi khi xóa tòa nhà: " + e.getMessage());
        }
    }


    public void handleBuildingMng(Pane paneBuildingManager, Pane paneApartManager, Pane paneSectorManager, Pane paneUserManager,
                                  TableView<BuildingDTO> buildingList, Pane... editPanes) {
        CommonUtils.showOnly(paneBuildingManager, paneApartManager, paneSectorManager, paneBuildingManager, paneUserManager);
        fetchDataFromApi(buildingList);
        CommonUtils.hideEditPanes(editPanes);
    }

    public void handleAddBuilding(Pane paneBuildingManager, Pane paneEditBuilding) {
        paneBuildingManager.setVisible(false);
        paneBuildingManager.setManaged(false);
        paneEditBuilding.setVisible(true);
        paneEditBuilding.setManaged(true);
    }

    public void handleEdit(TableView<BuildingDTO> buildingList, Pane paneBuildingManager, Pane paneEditBuilding,
                           TextField txtMaPhanKhu, TextField txtMaToaNha, TextField txtTenToaNha,
                           TextField txtDiaChiToaNha, TextField txtTenChuDauTuToaNha, TextField txtGiaBanThapNhat,
                           TextField txtGiaBanCaoNhat, TextField txtSoThangMay, TextField txtSoTangO,
                           TextField txtSoTangHam, TextField txtHangToaNha, TextField txtTongDienTichToaNha,
                           TextField txtPhiXeMay, TextField txtPhiOTo) {
        BuildingDTO selectedBuilding = buildingList.getSelectionModel().getSelectedItem();
        if (selectedBuilding == null) {
            CommonUtils.showAlert(Alert.AlertType.WARNING, "Chưa chọn tòa nhà", null,
                    "Vui lòng chọn một tòa nhà trong bảng để chỉnh sửa.");
            return;
        }

        selectedBuildingId = selectedBuilding.getId();
        paneBuildingManager.setVisible(false);
        paneBuildingManager.setManaged(false);
        paneEditBuilding.setVisible(true);
        paneEditBuilding.setManaged(true);

        SectorDTO sector = selectedBuilding.getSector();
        txtMaPhanKhu.setText(sector != null ? String.valueOf(sector.getId()) : "");
        txtMaToaNha.setText(selectedBuilding.getCode());
        txtTenToaNha.setText(selectedBuilding.getName());
        txtDiaChiToaNha.setText(selectedBuilding.getAddress());
        txtTenChuDauTuToaNha.setText(selectedBuilding.getDeveloperName());
        txtGiaBanThapNhat.setText(String.valueOf(selectedBuilding.getMinSellingPrice()));
        txtGiaBanCaoNhat.setText(String.valueOf(selectedBuilding.getMaxSellingPrice()));
        txtSoThangMay.setText(String.valueOf(selectedBuilding.getNumberEle()));
        txtSoTangO.setText(String.valueOf(selectedBuilding.getNumberLivingFloor()));
        txtSoTangHam.setText(String.valueOf(selectedBuilding.getNumberBasement()));
        txtHangToaNha.setText(selectedBuilding.getRank());
        txtTongDienTichToaNha.setText(String.valueOf(selectedBuilding.getTotalArea()));
        txtPhiXeMay.setText(String.valueOf(selectedBuilding.getBikeParkingMonthly()));
        txtPhiOTo.setText(String.valueOf(selectedBuilding.getCarParkingMonthly()));
    }

    public void handleUpdateBuilding(Pane paneBuildingManager, Pane paneEditBuilding, TableView<BuildingDTO> buildingList,
                                     TextField txtMaPhanKhu, TextField txtMaToaNha, TextField txtTenToaNha,
                                     TextField txtDiaChiToaNha, TextField txtTenChuDauTuToaNha, TextField txtGiaBanThapNhat,
                                     TextField txtGiaBanCaoNhat, TextField txtSoThangMay, TextField txtSoTangO,
                                     TextField txtSoTangHam, TextField txtHangToaNha, TextField txtTongDienTichToaNha,
                                     TextField txtPhiXeMay, TextField txtPhiOTo) {
        BuildingDTO buildingDTO = new BuildingDTO();

        try {
            buildingDTO.setCode(txtMaToaNha.getText());
            buildingDTO.setName(txtTenToaNha.getText());
            buildingDTO.setAddress(txtDiaChiToaNha.getText());
            buildingDTO.setDeveloperName(txtTenChuDauTuToaNha.getText());
            buildingDTO.setRank(txtHangToaNha.getText());
            buildingDTO.setTotalArea(CommonUtils.parseDouble(txtTongDienTichToaNha.getText()));
            buildingDTO.setMinSellingPrice(CommonUtils.parseLong(txtGiaBanThapNhat.getText()));
            buildingDTO.setMaxSellingPrice(CommonUtils.parseLong(txtGiaBanCaoNhat.getText()));
            buildingDTO.setNumberEle(CommonUtils.parseInteger(txtSoThangMay.getText()));
            buildingDTO.setNumberLivingFloor(CommonUtils.parseInteger(txtSoTangO.getText()));
            buildingDTO.setNumberBasement(CommonUtils.parseInteger(txtSoTangHam.getText()));
            buildingDTO.setBikeParkingMonthly(CommonUtils.parseLong(txtPhiXeMay.getText()));
            buildingDTO.setCarParkingMonthly(CommonUtils.parseLong(txtPhiOTo.getText()));

            Long sectorId = CommonUtils.parseLong(txtMaPhanKhu.getText());
            SectorDTO sectorDTO = new SectorDTO();
            sectorDTO.setId(sectorId);
            buildingDTO.setSector(sectorDTO);
        } catch (Exception e) {
            CommonUtils.showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Dữ liệu không hợp lệ",
                    "Vui lòng kiểm tra lại các trường nhập liệu.");
            return;
        }

        try {
            String jsonPayload = objectMapper.writeValueAsString(buildingDTO);
            String url = "http://localhost:8081/api/building/" + selectedBuildingId;

            HttpHeaders headers = CommonUtils.createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                CommonUtils.showAlert(Alert.AlertType.INFORMATION, "Cập nhật thành công",
                        "Tòa nhà đã được cập nhật thành công!", null);
                loadBuildingList(buildingList);
                paneBuildingManager.setVisible(true);
                paneBuildingManager.setManaged(true);
                paneEditBuilding.setVisible(false);
                paneEditBuilding.setManaged(false);
                CommonUtils.clearAllTextFieldsIn(paneEditBuilding);
            } else {
                CommonUtils.handleApiError(response.getStatusCode(), response.getBody());
            }
        } catch (HttpClientErrorException | HttpServerErrorException httpEx) {
            CommonUtils.showAlert(Alert.AlertType.ERROR, "Cập nhật thất bại",
                    "Lỗi từ phía máy chủ: " + httpEx.getStatusCode(), httpEx.getResponseBodyAsString());
        } catch (Exception ex) {
            CommonUtils.showAlert(Alert.AlertType.ERROR, "Cập nhật thất bại",
                    "Đã xảy ra lỗi không xác định.", ex.getMessage());
        }
    }

    public void handleAddBuilding_2(Pane paneBuildingManager, Pane paneEditBuilding, TableView<BuildingDTO> buildingList,
                                    TextField txtMaPhanKhu, TextField txtMaToaNha, TextField txtTenToaNha,
                                    TextField txtDiaChiToaNha, TextField txtTenChuDauTuToaNha, TextField txtGiaBanThapNhat,
                                    TextField txtGiaBanCaoNhat, TextField txtSoThangMay, TextField txtSoTangO,
                                    TextField txtSoTangHam, TextField txtHangToaNha, TextField txtTongDienTichToaNha,
                                    TextField txtPhiXeMay, TextField txtPhiOTo) {
        BuildingDTO buildingDTO = new BuildingDTO();

        try {
            buildingDTO.setCode(txtMaToaNha.getText());
            buildingDTO.setName(txtTenToaNha.getText());
            buildingDTO.setAddress(txtDiaChiToaNha.getText());
            buildingDTO.setDeveloperName(txtTenChuDauTuToaNha.getText());
            buildingDTO.setRank(txtHangToaNha.getText());
            buildingDTO.setTotalArea(CommonUtils.parseDouble(txtTongDienTichToaNha.getText()));
            buildingDTO.setMinSellingPrice(CommonUtils.parseLong(txtGiaBanThapNhat.getText()));
            buildingDTO.setMaxSellingPrice(CommonUtils.parseLong(txtGiaBanCaoNhat.getText()));
            buildingDTO.setNumberEle(CommonUtils.parseInteger(txtSoThangMay.getText()));
            buildingDTO.setNumberLivingFloor(CommonUtils.parseInteger(txtSoTangO.getText()));
            buildingDTO.setNumberBasement(CommonUtils.parseInteger(txtSoTangHam.getText()));
            buildingDTO.setBikeParkingMonthly(CommonUtils.parseLong(txtPhiXeMay.getText()));
            buildingDTO.setCarParkingMonthly(CommonUtils.parseLong(txtPhiOTo.getText()));

            Long sectorId = CommonUtils.parseLong(txtMaPhanKhu.getText());
            SectorDTO sectorDTO = new SectorDTO();
            sectorDTO.setId(sectorId);
            buildingDTO.setSector(sectorDTO);
        } catch (Exception e) {
            CommonUtils.showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Thông tin không hợp lệ",
                    "Vui lòng kiểm tra lại các trường số.");
            return;
        }

        try {
            String jsonPayload = objectMapper.writeValueAsString(buildingDTO);
            String url = "http://localhost:8081/api/building";
            HttpHeaders headers = CommonUtils.createAuthHeaders();
            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                CommonUtils.showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã thêm tòa nhà mới!", null);
                loadBuildingList(buildingList);
                paneBuildingManager.setVisible(true);
                paneBuildingManager.setManaged(true);
                paneEditBuilding.setVisible(false);
                paneEditBuilding.setManaged(false);
                CommonUtils.clearAllTextFieldsIn(paneEditBuilding);
            } else {
                CommonUtils.handleApiError(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            CommonUtils.showError("Lỗi kết nối hoặc xử lý: " + e.getMessage());
        }
    }

    public void loadBuildingList(TableView<BuildingDTO> buildingList) {
        String url = "http://localhost:8081/api/building";

        try {
            HttpHeaders headers = CommonUtils.createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<BuildingDTO> buildingListData = objectMapper.readValue(
                        response.getBody(),
                        new TypeReference<>() {}
                );
                buildingList.setItems(FXCollections.observableArrayList(buildingListData));
            } else {
                CommonUtils.showError("Không thể tải danh sách: " + response.getStatusCode());
            }
        } catch (Exception e) {
            CommonUtils.showError("Lỗi khi tải danh sách tòa nhà: " + e.getMessage());
        }
    }

    public void handleBackBuilding(Pane paneBuildingManager, Pane paneEditBuilding) {
        paneBuildingManager.setVisible(true);
        paneBuildingManager.setManaged(true);
        paneEditBuilding.setVisible(false);
        paneEditBuilding.setManaged(false);
        CommonUtils.clearAllTextFieldsIn(paneEditBuilding);
    }

    public void handleBackBuilding_2(Pane paneBuildingManager, Pane paneChangePwd) {
        paneBuildingManager.setVisible(true);
        paneBuildingManager.setManaged(true);
        paneChangePwd.setVisible(false);
        paneChangePwd.setManaged(false);
        CommonUtils.clearAllTextFieldsIn(paneChangePwd);
    }
}
