package com.example.demo.views;

import com.example.demo.model.dto.SectorDTO;
import com.example.demo.views.CommonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.Pane;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.List;

public class SectorManagerView {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private Long selectedSectorId;

    public SectorManagerView() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    public void setUpSectorTableColumns(TableColumn<SectorDTO, String> maPK, TableColumn<SectorDTO, String> tenPK,
                                       TableColumn<SectorDTO, String> viTriPK, TableColumn<SectorDTO, Double> tongDTPK,
                                       TableColumn<SectorDTO, Integer> soTangPK, TableColumn<SectorDTO, String> moTaPK,
                                       TableColumn<SectorDTO, String> quanLyPK, TableColumn<SectorDTO, String> trangThaiPK,
                                       TableColumn<SectorDTO, String> startDatePK, TableColumn<SectorDTO, String> endDatePK) {
        maPK.setCellValueFactory(new PropertyValueFactory<>("code"));
        tenPK.setCellValueFactory(new PropertyValueFactory<>("name"));
        viTriPK.setCellValueFactory(new PropertyValueFactory<>("location"));
        tongDTPK.setCellValueFactory(new PropertyValueFactory<>("totalArea"));
        soTangPK.setCellValueFactory(new PropertyValueFactory<>("maxFloors"));
        moTaPK.setCellValueFactory(new PropertyValueFactory<>("description"));
        quanLyPK.setCellValueFactory(new PropertyValueFactory<>("managerName"));
        trangThaiPK.setCellValueFactory(new PropertyValueFactory<>("status"));

        startDatePK.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getStartDate() != null ? dateFormatter.format(cellData.getValue().getStartDate()) : ""));
        endDatePK.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getExpectedCompletionDate() != null ? dateFormatter.format(cellData.getValue().getExpectedCompletionDate()) : ""));
    }

    public void fetchSectorDataFromApi(TableView<SectorDTO> sectorList) {
        try {
            String url = "http://localhost:8081/api/sector";
            HttpHeaders headers = CommonUtils.createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<SectorDTO> sectors = objectMapper.readValue(
                        response.getBody(),
                        new TypeReference<>() {}
                );
                sectorList.setItems(FXCollections.observableArrayList(sectors));
            } else {
                CommonUtils.showError("Không thể lấy dữ liệu phân khu: " + response.getStatusCode());
            }
        } catch (Exception e) {
            CommonUtils.showError("Lỗi khi gọi API phân khu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadSectorList(TableView<SectorDTO> sectorList) {
        String url = "http://localhost:8081/api/sector";
        try {
            HttpHeaders headers = CommonUtils.createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                List<SectorDTO> sectors = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
                sectorList.setItems(FXCollections.observableArrayList(sectors));
            } else {
                CommonUtils.handleApiError(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            CommonUtils.showError("Lỗi khi tải danh sách phân khu: " + e.getMessage());
        }
    }

    public SectorDTO getSectorInputFromForm(TextField txtMaPhanKhu, TextField txtTenPhanKhu, TextField txtViTri,
                                           TextField txtTongDienTich, TextField txtMoTa, TextField txtTenQuanLy,
                                           TextField txtSoTangCaoNhat, TextField txtNgayKhoiCong, TextField txtNgayHoanThanh,
                                           ComboBox<String> comboTrangThai) {
        try {
            SectorDTO dto = new SectorDTO();
            dto.setCode(txtMaPhanKhu.getText());
            dto.setName(txtTenPhanKhu.getText());
            dto.setLocation(txtViTri.getText());
            dto.setTotalArea(Double.parseDouble(txtTongDienTich.getText()));
            dto.setDescription(txtMoTa.getText());
            dto.setManagerName(txtTenQuanLy.getText());
            dto.setMaxFloors(Integer.parseInt(txtSoTangCaoNhat.getText()));
            dto.setStatus(comboTrangThai.getValue());

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            dto.setStartDate(dateFormat.parse(txtNgayKhoiCong.getText()));
            dto.setExpectedCompletionDate(dateFormat.parse(txtNgayHoanThanh.getText()));

            return dto;
        } catch (Exception e) {
            CommonUtils.showError("Vui lòng nhập đúng định dạng ngày: yyyy-MM-dd và các trường số.");
            return null;
        }
    }

    public void handleAddSector(Pane paneSectorManager, Pane paneEditSector) {
        paneSectorManager.setVisible(false);
        paneSectorManager.setManaged(false);
        paneEditSector.setVisible(true);
        paneEditSector.setManaged(true);
    }

    public void handleAddSector_2(Pane paneSectorManager, Pane paneEditSector, TableView<SectorDTO> sectorList,
                                  TextField txtMaPhanKhu, TextField txtTenPhanKhu, TextField txtViTri,
                                  TextField txtTongDienTich, TextField txtMoTa, TextField txtTenQuanLy,
                                  TextField txtSoTangCaoNhat, TextField txtNgayKhoiCong, TextField txtNgayHoanThanh,
                                  ComboBox<String> comboTrangThai) {
        SectorDTO sector = getSectorInputFromForm(txtMaPhanKhu, txtTenPhanKhu, txtViTri, txtTongDienTich, txtMoTa,
                txtTenQuanLy, txtSoTangCaoNhat, txtNgayKhoiCong, txtNgayHoanThanh, comboTrangThai);
        if (sector == null) return;

        try {
            String json = objectMapper.writeValueAsString(sector);
            HttpHeaders headers = CommonUtils.createAuthHeaders();
            HttpEntity<String> request = new HttpEntity<>(json, headers);

            ResponseEntity<String> response = restTemplate.exchange("http://localhost:8081/api/sector", HttpMethod.POST, request, String.class);
            if (response.getStatusCode() == HttpStatus.CREATED) {
                CommonUtils.showSuccess("Đã thêm phân khu!");
                backToSectorManager(paneSectorManager, paneEditSector);
                CommonUtils.clearAllTextFieldsIn(paneEditSector);
                loadSectorList(sectorList);
            } else {
                CommonUtils.handleApiError(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            CommonUtils.showError("Lỗi khi thêm phân khu: " + e.getMessage());
        }
    }

    public void handleUpdateSector(Pane paneSectorManager, Pane paneEditSector, TableView<SectorDTO> sectorList,
                                   TextField txtMaPhanKhu, TextField txtTenPhanKhu, TextField txtViTri,
                                   TextField txtTongDienTich, TextField txtMoTa, TextField txtTenQuanLy,
                                   TextField txtSoTangCaoNhat, TextField txtNgayKhoiCong, TextField txtNgayHoanThanh,
                                   ComboBox<String> comboTrangThai) {
        SectorDTO sector = getSectorInputFromForm(txtMaPhanKhu, txtTenPhanKhu, txtViTri, txtTongDienTich, txtMoTa,
                txtTenQuanLy, txtSoTangCaoNhat, txtNgayKhoiCong, txtNgayHoanThanh, comboTrangThai);
        if (sector == null) return;

        try {
            String json = objectMapper.writeValueAsString(sector);
            HttpHeaders headers = CommonUtils.createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(json, headers);

            String url = "http://localhost:8081/api/sector/" + selectedSectorId;
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                CommonUtils.showSuccess("Đã cập nhật phân khu!");
                backToSectorManager(paneSectorManager, paneEditSector);
                CommonUtils.clearAllTextFieldsIn(paneEditSector);
                loadSectorList(sectorList);
            } else {
                CommonUtils.handleApiError(response.getStatusCode(), response.getBody());
            }
        } catch (Exception e) {
            CommonUtils.showError("Lỗi khi cập nhật phân khu: " + e.getMessage());
        }
    }

    public void handleEditSector(TableView<SectorDTO> sectorList, Pane paneSectorManager, Pane paneEditSector,
                                 TextField txtMaPhanKhu, TextField txtTenPhanKhu, TextField txtViTri,
                                 TextField txtTongDienTich, TextField txtMoTa, TextField txtTenQuanLy,
                                 TextField txtSoTangCaoNhat, TextField txtNgayKhoiCong, TextField txtNgayHoanThanh,
                                 ComboBox<String> comboTrangThai) {
        SectorDTO selected = sectorList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            CommonUtils.showError("Chưa chọn phân khu!");
            return;
        }

        selectedSectorId = selected.getId();
        paneSectorManager.setVisible(false);
        paneSectorManager.setManaged(false);
        paneEditSector.setVisible(true);
        paneEditSector.setManaged(true);

        txtMaPhanKhu.setText(selected.getCode());
        txtTenPhanKhu.setText(selected.getName());
        txtViTri.setText(selected.getLocation());
        txtTongDienTich.setText(String.valueOf(selected.getTotalArea()));
        txtMoTa.setText(selected.getDescription());
        txtTenQuanLy.setText(selected.getManagerName());
        txtSoTangCaoNhat.setText(String.valueOf(selected.getMaxFloors()));

        txtNgayKhoiCong.setText(
                selected.getStartDate() != null ? dateFormatter.format(selected.getStartDate()) : "");
        txtNgayHoanThanh.setText(
                selected.getExpectedCompletionDate() != null ? dateFormatter.format(selected.getExpectedCompletionDate()) : "");

        comboTrangThai.setValue(selected.getStatus());
    }

    public void handleDeleteSector(TableView<SectorDTO> sectorList) {
        SectorDTO selected = sectorList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            CommonUtils.showError("Vui lòng chọn phân khu cần xóa!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText("Xóa phân khu " + selected.getName() + "?");
        confirm.setContentText("Thao tác này không thể hoàn tác.");

        confirm.showAndWait().ifPresent(result -> {
            if (result.getText().equals("OK")) {
                try {
                    String url = "http://localhost:8081/api/sector/" + selected.getId();
                    HttpHeaders headers = CommonUtils.createAuthHeaders();
                    HttpEntity<Void> entity = new HttpEntity<>(headers);
                    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);

                    if (response.getStatusCode() == HttpStatus.OK) {
                        CommonUtils.showSuccess("Đã xóa thành công!");
                        loadSectorList(sectorList);
                    } else {
                        CommonUtils.handleApiError(response.getStatusCode(), response.getBody());
                    }
                } catch (Exception e) {
                    CommonUtils.showError("Lỗi khi xóa: " + e.getMessage());
                }
            }
        });
    }

    public void handleSectorMng(Pane paneSectorManager, Pane paneApartManager, Pane paneBuildingManager, Pane paneUserManager,
                                TableView<SectorDTO> sectorList, Pane... editPanes) {
        CommonUtils.showOnly(paneSectorManager, paneApartManager, paneSectorManager, paneBuildingManager, paneUserManager);
        loadSectorList(sectorList);
        CommonUtils.hideEditPanes(editPanes);
    }

    public void backToSectorManager(Pane paneSectorManager, Pane paneEditSector) {
        paneSectorManager.setVisible(true);
        paneSectorManager.setManaged(true);
        paneEditSector.setVisible(false);
        paneEditSector.setManaged(false);
        CommonUtils.clearAllTextFieldsIn(paneEditSector);
    }
}
