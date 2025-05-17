package com.example.demo.views;

import com.example.demo.model.dto.ApartmentDTO;
import com.example.demo.model.dto.BuildingDTO;
import com.example.demo.views.CommonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class ApartmentManagerView {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Long selectedApartmentId;

    public ApartmentManagerView() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    public void setUpTableColumns(TableView<ApartmentDTO> apartList, TableColumn<ApartmentDTO, String> colCode,
                                  TableColumn<ApartmentDTO, String> colName, TableColumn<ApartmentDTO, Integer> colFloor,
                                  TableColumn<ApartmentDTO, Double> colArea, TableColumn<ApartmentDTO, Integer> colBedrooms,
                                  TableColumn<ApartmentDTO, Integer> colBathrooms, TableColumn<ApartmentDTO, Double> colPrice,
                                  TableColumn<ApartmentDTO, String> colStatus, TableColumn<ApartmentDTO, String> colFacing,
                                  TableColumn<ApartmentDTO, String> colDescription, TableColumn<ApartmentDTO, Double> colElectricity,
                                  TableColumn<ApartmentDTO, Double> colWater, TableColumn<ApartmentDTO, Double> colCeiling) {
        colCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colFloor.setCellValueFactory(new PropertyValueFactory<>("floor"));
        colArea.setCellValueFactory(new PropertyValueFactory<>("area"));
        colBedrooms.setCellValueFactory(new PropertyValueFactory<>("numberOfBedrooms"));
        colBathrooms.setCellValueFactory(new PropertyValueFactory<>("numberOfBathrooms"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colFacing.setCellValueFactory(new PropertyValueFactory<>("facingDirection"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colElectricity.setCellValueFactory(new PropertyValueFactory<>("electricityPricePerKwh"));
        colWater.setCellValueFactory(new PropertyValueFactory<>("waterPricePerM3"));
        colCeiling.setCellValueFactory(new PropertyValueFactory<>("ceilingHeight"));
    }

    public void handleApartMng(Pane paneApartManager, Pane paneBuildingManager, Pane paneSectorManager, Pane paneUserManager,
            TableView<ApartmentDTO> apartList, Pane... editPanes) {
    		CommonUtils.showOnly(paneApartManager, paneApartManager, paneSectorManager, paneBuildingManager, paneUserManager);
    		fetchDataFromApi(apartList);
    		CommonUtils.hideEditPanes(editPanes);
}
    
    public void fetchDataFromApi(TableView<ApartmentDTO> apartList) {
        try {
            String url = "http://localhost:8081/api/apartment";
            HttpHeaders headers = CommonUtils.createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<ApartmentDTO> apartments = objectMapper.readValue(
                        response.getBody(),
                        new TypeReference<>() {}
                );
                apartList.setItems(FXCollections.observableArrayList(apartments));
                System.out.println("Đã tải " + apartments.size() + " căn hộ.");
            } else {
                CommonUtils.showAlert(Alert.AlertType.ERROR, "Lỗi tải dữ liệu",
                        "Không thể lấy dữ liệu căn hộ: ", String.valueOf(response.getStatusCode()));
            }
        } catch (Exception e) {
            CommonUtils.showAlert(Alert.AlertType.ERROR, "Lỗi tải dữ liệu",
                    "Lỗi khi gọi API căn hộ: ", e.getMessage());
            e.printStackTrace();
        }
    }

    public void handleAddApart(Pane paneApartManager, Pane paneEditApart) {
        paneApartManager.setVisible(false);
        paneApartManager.setManaged(false);
        paneEditApart.setVisible(true);
        paneEditApart.setManaged(true);
        CommonUtils.clearAllTextFieldsIn(paneEditApart);
    }

    public void handleEditApart(TableView<ApartmentDTO> apartList, Pane paneApartManager, Pane paneEditApart,
                                TextField txtMaToaNha_CH, TextField txtMaCanHo, TextField txtTenCanHo,
                                TextField txtSoTangCH, TextField txtDienTichCH, TextField txtSoPhongNgu,
                                TextField txtSoPhongTam, TextField txtGiaCH, ComboBox<String> cbbTrangThaiCH,
                                TextField txtHuongCH, TextField txtMoTaCH, TextField txtViewCH,
                                TextField txtTienDienCH, TextField txtTienNuocCH, TextField txtChieuCaoCH) {
        ApartmentDTO selectedApartment = apartList.getSelectionModel().getSelectedItem();
        if (selectedApartment == null) {
            CommonUtils.showAlert(Alert.AlertType.WARNING, "Chưa chọn căn hộ",
                    null, "Vui lòng chọn một căn hộ trong bảng để chỉnh sửa.");
            return;
        }

        selectedApartmentId = selectedApartment.getId();
        paneApartManager.setVisible(false);
        paneApartManager.setManaged(false);
        paneEditApart.setVisible(true);
        paneEditApart.setManaged(true);

        BuildingDTO building = selectedApartment.getBuilding();
        txtMaToaNha_CH.setText(building != null ? String.valueOf(building.getId()) : "");
        txtMaCanHo.setText(selectedApartment.getCode());
        txtTenCanHo.setText(selectedApartment.getName());
        txtSoTangCH.setText(String.valueOf(selectedApartment.getFloor()));
        txtDienTichCH.setText(String.valueOf(selectedApartment.getArea()));
        txtSoPhongNgu.setText(String.valueOf(selectedApartment.getNumberOfBedrooms()));
        txtSoPhongTam.setText(String.valueOf(selectedApartment.getNumberOfBathrooms()));
        txtGiaCH.setText(String.valueOf(selectedApartment.getPrice()));
        cbbTrangThaiCH.setValue(selectedApartment.getStatus());
        txtHuongCH.setText(selectedApartment.getFacingDirection());
        txtMoTaCH.setText(selectedApartment.getDescription());
        txtViewCH.setText(selectedApartment.getView());
        txtTienDienCH.setText(String.valueOf(selectedApartment.getElectricityPricePerKwh()));
        txtTienNuocCH.setText(String.valueOf(selectedApartment.getWaterPricePerM3()));
        txtChieuCaoCH.setText(String.valueOf(selectedApartment.getCeilingHeight()));
    }

    public void handleAddApart_2(Pane paneApartManager, Pane paneEditApart, TableView<ApartmentDTO> apartList,
                                 TextField txtMaToaNha_CH, TextField txtMaCanHo, TextField txtTenCanHo,
                                 TextField txtSoTangCH, TextField txtDienTichCH, TextField txtSoPhongNgu,
                                 TextField txtSoPhongTam, TextField txtGiaCH, ComboBox<String> cbbTrangThaiCH,
                                 TextField txtHuongCH, TextField txtMoTaCH, TextField txtViewCH,
                                 TextField txtTienDienCH, TextField txtTienNuocCH, TextField txtChieuCaoCH) {
        ApartmentDTO apartmentDTO = getApartmentInputFromForm(txtMaToaNha_CH, txtMaCanHo, txtTenCanHo, txtSoTangCH,
                txtDienTichCH, txtSoPhongNgu, txtSoPhongTam, txtGiaCH, cbbTrangThaiCH, txtHuongCH, txtMoTaCH,
                txtViewCH, txtTienDienCH, txtTienNuocCH, txtChieuCaoCH);
        if (apartmentDTO == null) return;

        try {
            String jsonPayload = objectMapper.writeValueAsString(apartmentDTO);
            String url = "http://localhost:8081/api/apartment";
            HttpHeaders headers = CommonUtils.createAuthHeaders();
            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            if (response.getStatusCode() == HttpStatus.CREATED) {
                CommonUtils.showAlert(Alert.AlertType.INFORMATION, "Thành công",
                        "Đã thêm căn hộ mới!", null);
                fetchDataFromApi(apartList);
                backToApartManager(paneApartManager, paneEditApart);
            } else {
                CommonUtils.handleApiError(response.getStatusCode(), response.getBody());
            }
        } catch (HttpClientErrorException | HttpServerErrorException httpEx) {
            CommonUtils.showAlert(Alert.AlertType.ERROR, "Thêm thất bại",
                    "Lỗi từ phía máy chủ: " + httpEx.getStatusCode(), httpEx.getResponseBodyAsString());
        } catch (Exception e) {
            CommonUtils.showAlert(Alert.AlertType.ERROR, "Thêm thất bại",
                    "Đã xảy ra lỗi không xác định.", e.getMessage());
        }
    }

    public void handleUpdateApart(Pane paneApartManager, Pane paneEditApart, TableView<ApartmentDTO> apartList,
                                  TextField txtMaToaNha_CH, TextField txtMaCanHo, TextField txtTenCanHo,
                                  TextField txtSoTangCH, TextField txtDienTichCH, TextField txtSoPhongNgu,
                                  TextField txtSoPhongTam, TextField txtGiaCH, ComboBox<String> cbbTrangThaiCH,
                                  TextField txtHuongCH, TextField txtMoTaCH, TextField txtViewCH,
                                  TextField txtTienDienCH, TextField txtTienNuocCH, TextField txtChieuCaoCH) {
        ApartmentDTO apartmentDTO = getApartmentInputFromForm(txtMaToaNha_CH, txtMaCanHo, txtTenCanHo, txtSoTangCH,
                txtDienTichCH, txtSoPhongNgu, txtSoPhongTam, txtGiaCH, cbbTrangThaiCH, txtHuongCH, txtMoTaCH,
                txtViewCH, txtTienDienCH, txtTienNuocCH, txtChieuCaoCH);
        if (apartmentDTO == null) return;

        try {
            String jsonPayload = objectMapper.writeValueAsString(apartmentDTO);
            String url = "http://localhost:8081/api/apartment/" + selectedApartmentId;
            HttpHeaders headers = CommonUtils.createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                CommonUtils.showAlert(Alert.AlertType.INFORMATION, "Cập nhật thành công",
                        "Căn hộ đã được cập nhật thành công!", null);
                fetchDataFromApi(apartList);
                backToApartManager(paneApartManager, paneEditApart);
            } else {
                CommonUtils.handleApiError(response.getStatusCode(), response.getBody());
            }
        } catch (HttpClientErrorException | HttpServerErrorException httpEx) {
            CommonUtils.showAlert(Alert.AlertType.ERROR, "Cập nhật thất bại",
                    "Lỗi từ phía máy chủ: " + httpEx.getStatusCode(), httpEx.getResponseBodyAsString());
        } catch (Exception e) {
            CommonUtils.showAlert(Alert.AlertType.ERROR, "Cập nhật thất bại",
                    "Đã xảy ra lỗi không xác định.", e.getMessage());
        }
    }

    public void handleDeleteApart(TableView<ApartmentDTO> apartList) {
        ApartmentDTO selected = apartList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            CommonUtils.showAlert(Alert.AlertType.WARNING, "Chưa chọn căn hộ",
                    null, "Vui lòng chọn một căn hộ cần xóa!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText("Xóa căn hộ " + selected.getName() + "?");
        confirm.setContentText("Thao tác này không thể hoàn tác.");

        confirm.showAndWait().ifPresent(result -> {
            if (result.getText().equals("OK")) {
                try {
                    String url = "http://localhost:8081/api/apartment/" + selected.getId();
                    HttpHeaders headers = CommonUtils.createAuthHeaders();
                    HttpEntity<Void> entity = new HttpEntity<>(headers);
                    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);

                    if (response.getStatusCode() == HttpStatus.OK) {
                        CommonUtils.showAlert(Alert.AlertType.INFORMATION, "Xóa thành công",
                                "Đã xóa căn hộ thành công!", null);
                        fetchDataFromApi(apartList);
                    } else {
                        CommonUtils.handleApiError(response.getStatusCode(), response.getBody());
                    }
                } catch (Exception e) {
                    CommonUtils.showAlert(Alert.AlertType.ERROR, "Xóa thất bại",
                            "Lỗi khi xóa căn hộ: ", e.getMessage());
                }
            }
        });
    }

    public void handleSearchByFloor(TableView<ApartmentDTO> apartList, TextField txtSearchByFloor) {
        String floorStr = txtSearchByFloor.getText().trim();
        if (floorStr.isEmpty()) {
            fetchDataFromApi(apartList);
            return;
        }

        try {
            Integer floor = Integer.parseInt(floorStr);
            String url = "http://localhost:8081/api/apartment/floor/" + floor;
            HttpHeaders headers = CommonUtils.createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<ApartmentDTO> apartments = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
                apartList.setItems(FXCollections.observableArrayList(apartments));
            } else {
                CommonUtils.showAlert(Alert.AlertType.WARNING, "Không tìm thấy",
                        "Không tìm thấy căn hộ ở tầng này: ", String.valueOf(response.getStatusCode()));
            }
        } catch (NumberFormatException e) {
            CommonUtils.showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu",
                    "Tầng phải là số nguyên!", null);
        } catch (Exception e) {
            CommonUtils.showAlert(Alert.AlertType.ERROR, "Lỗi tìm kiếm",
                    "Lỗi khi tìm kiếm theo tầng: ", e.getMessage());
        }
    }

    public void handleSearchByStatus(TableView<ApartmentDTO> apartList, ComboBox<String> cbbSearchByStatus) {
        String status = cbbSearchByStatus.getValue();
        if (status == null || status.isEmpty()) {
            fetchDataFromApi(apartList);
            CommonUtils.showAlert(Alert.AlertType.WARNING, "Chưa chọn trạng thái",
                    null, "Vui lòng chọn một trạng thái để tìm kiếm!");
            return;
        }

        try {
            String url = "http://localhost:8081/api/apartment/status/" + status;
            HttpHeaders headers = CommonUtils.createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<ApartmentDTO> apartments = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
                apartList.setItems(FXCollections.observableArrayList(apartments));
            } else {
                CommonUtils.showAlert(Alert.AlertType.WARNING, "Không tìm thấy",
                        "Không tìm thấy căn hộ với trạng thái này: ", String.valueOf(response.getStatusCode()));
            }
        } catch (Exception e) {
            CommonUtils.showAlert(Alert.AlertType.ERROR, "Lỗi tìm kiếm",
                    "Lỗi khi tìm kiếm theo trạng thái: ", e.getMessage());
        }
    }

    private ApartmentDTO getApartmentInputFromForm(TextField txtMaToaNha_CH, TextField txtMaCanHo, TextField txtTenCanHo,
                                                  TextField txtSoTangCH, TextField txtDienTichCH, TextField txtSoPhongNgu,
                                                  TextField txtSoPhongTam, TextField txtGiaCH, ComboBox<String> cbbTrangThaiCH,
                                                  TextField txtHuongCH, TextField txtMoTaCH, TextField txtViewCH,
                                                  TextField txtTienDienCH, TextField txtTienNuocCH, TextField txtChieuCaoCH) {
        try {
            // Kiểm tra các trường bắt buộc
            if (txtMaCanHo.getText().isEmpty() || txtTenCanHo.getText().isEmpty() ||
                txtSoTangCH.getText().isEmpty() || txtDienTichCH.getText().isEmpty() ||
                cbbTrangThaiCH.getValue() == null) {
                CommonUtils.showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu",
                        "Dữ liệu không hợp lệ", "Vui lòng nhập đầy đủ các trường bắt buộc.");
                return null;
            }

            ApartmentDTO dto = new ApartmentDTO();
            Long buildingId = CommonUtils.parseLong(txtMaToaNha_CH.getText());
            BuildingDTO buildingDTO = new BuildingDTO();
            buildingDTO.setId(buildingId);
            dto.setBuilding(buildingDTO);
            dto.setCode(txtMaCanHo.getText());
            dto.setName(txtTenCanHo.getText());
            dto.setFloor(CommonUtils.parseInteger(txtSoTangCH.getText()));
            dto.setArea(CommonUtils.parseDouble(txtDienTichCH.getText()));
            dto.setNumberOfBedrooms(txtSoPhongNgu.getText().isEmpty() ? null : CommonUtils.parseInteger(txtSoPhongNgu.getText()));
            dto.setNumberOfBathrooms(txtSoPhongTam.getText().isEmpty() ? null : CommonUtils.parseInteger(txtSoPhongTam.getText()));
            dto.setPrice(txtGiaCH.getText().isEmpty() ? null : CommonUtils.parseLong(txtGiaCH.getText()));
            dto.setStatus(cbbTrangThaiCH.getValue());
            dto.setFacingDirection(txtHuongCH.getText());
            dto.setDescription(txtMoTaCH.getText());
            dto.setView(txtViewCH.getText());
            dto.setElectricityPricePerKwh(txtTienDienCH.getText().isEmpty() ? null : CommonUtils.parseInteger(txtTienDienCH.getText()));
            dto.setWaterPricePerM3(txtTienNuocCH.getText().isEmpty() ? null : CommonUtils.parseInteger(txtTienNuocCH.getText()));
            dto.setCeilingHeight(txtChieuCaoCH.getText().isEmpty() ? null : CommonUtils.parseDouble(txtChieuCaoCH.getText()));
            return dto;
        } catch (Exception e) {
            CommonUtils.showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu",
                    "Dữ liệu không hợp lệ", "Vui lòng kiểm tra lại các trường nhập liệu.");
            return null;
        }
    }

    public void backToApartManager(Pane paneApartManager, Pane paneEditApart) {
        paneApartManager.setVisible(true);
        paneApartManager.setManaged(true);
        paneEditApart.setVisible(false);
        paneEditApart.setManaged(false);
        CommonUtils.clearAllTextFieldsIn(paneEditApart);
    }
}