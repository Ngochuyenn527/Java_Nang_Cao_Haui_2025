package com.example.demo.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import com.example.demo.model.dto.BuildingDTO;
import com.example.demo.model.response.BuildingSearchResponse;

import java.net.URLEncoder;
import java.util.Base64;
import java.util.List;

public class BuildingManagerViewController {

    @FXML
    private TextField txtname, txtaddress, txtrank, txtareaFrom, txtareaTo, txtsellingPrice, txtnumberLivingFloor, txtnumberBasement;
    @FXML
    private Button btnSearch, btnAdd, btnEdit, btnDelete, btnfet;
    @FXML
    private TableView<BuildingSearchResponse> buildingList;
    @FXML
    private TableColumn<BuildingSearchResponse, String> name, address, rank, developerName;
    @FXML
    private TableColumn<BuildingSearchResponse, Double> totalArea;
    @FXML
    private TableColumn<BuildingSearchResponse, Long> minSellingPrice, maxSellingPrice;
    @FXML
    private TableColumn<BuildingSearchResponse, Integer> numberEle, numberLivingFloor, numberBasement;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    public void initialize() {
        setUpTableColumns();
        fetchDataFromApi(); // Fetch data on initialization
    }

    private void setUpTableColumns() {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        address.setCellValueFactory(new PropertyValueFactory<>("address"));
        rank.setCellValueFactory(new PropertyValueFactory<>("rank"));
        developerName.setCellValueFactory(new PropertyValueFactory<>("developerName"));
        totalArea.setCellValueFactory(new PropertyValueFactory<>("totalArea"));
        minSellingPrice.setCellValueFactory(new PropertyValueFactory<>("minSellingPrice"));
        maxSellingPrice.setCellValueFactory(new PropertyValueFactory<>("maxSellingPrice"));
        numberEle.setCellValueFactory(new PropertyValueFactory<>("numberEle"));
        numberLivingFloor.setCellValueFactory(new PropertyValueFactory<>("numberLivingFloor"));
        numberBasement.setCellValueFactory(new PropertyValueFactory<>("numberBasement"));
    }

    @FXML
    private void fetchDataFromApi() {
        try {
            String url = "http://localhost:8081/api/building";
            HttpHeaders headers = createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<BuildingSearchResponse> buildings = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
                buildingList.setItems(FXCollections.observableArrayList(buildings));
            } else {
                handleApiError(response);
            }
        } catch (Exception e) {
            showError("Lỗi khi gọi API: " + e.getMessage());
        }
    }

    private HttpHeaders createAuthHeaders() {
        String auth = "admin:123456";
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);
        return headers;
    }

    private void handleApiError(ResponseEntity<String> response) {
        if (response.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            showError("Yêu cầu xác thực - Sai tên người dùng hoặc mật khẩu!");
        } else if (response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
            showError("Lỗi máy chủ: API gặp sự cố.");
        } else {
            showError("Lỗi khi gọi API: " + response.getStatusCode());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void handleSearch() {
        String query = buildSearchQuery();
        if (query.isEmpty()) return;

        try {
            String url = "http://localhost:8081/api/building?" + query;
            HttpHeaders headers = createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<BuildingSearchResponse> buildings = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
                buildingList.setItems(FXCollections.observableArrayList(buildings));
            } else {
                handleApiError(response);
            }
        } catch (Exception e) {
            showError("Lỗi khi gọi API: " + e.getMessage());
        }
    }

    private String buildSearchQuery() {
        StringBuilder query = new StringBuilder();
        appendQueryParam(query, "name", txtname);
        appendQueryParam(query, "address", txtaddress);
        appendQueryParam(query, "rank", txtrank);
        appendNumericQueryParam(query, "areaFrom", txtareaFrom);
        appendNumericQueryParam(query, "areaTo", txtareaTo);
        appendNumericQueryParam(query, "sellingPrice", txtsellingPrice);
        appendNumericQueryParam(query, "numberLivingFloor", txtnumberLivingFloor);
        appendNumericQueryParam(query, "numberBasement", txtnumberBasement);
        return query.toString();
    }

    private void appendQueryParam(StringBuilder query, String param, TextField textField) {
        if (!textField.getText().isEmpty()) {
            try {
                query.append(param).append("=").append(URLEncoder.encode(textField.getText(), "UTF-8")).append("&");
            } catch (Exception e) {
                showError("Lỗi khi mã hóa tham số: " + e.getMessage());
            }
        }
    }

    private void appendNumericQueryParam(StringBuilder query, String param, TextField textField) {
        if (!textField.getText().isEmpty()) {
            try {
                Double.parseDouble(textField.getText());
                query.append(param).append("=").append(textField.getText()).append("&");
            } catch (NumberFormatException e) {
                showError(param + " phải là số hợp lệ!");
            }
        }
    }

    @FXML
    private void handleEdit() {
        BuildingSearchResponse selected = buildingList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Chưa chọn tòa nhà nào để sửa!");
            return;
        }

        fetchBuildingDetails(selected.getId(), (buildingDto) -> {
            Platform.runLater(() -> openEditWindow(buildingDto, selected.getId()));
        });
    }

    private void fetchBuildingDetails(Long buildingId, java.util.function.Consumer<BuildingDTO> callback) {
        String url = "http://localhost:8081/api/building/" + buildingId;
        try {
            HttpHeaders headers = createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                BuildingDTO buildingDto = objectMapper.readValue(response.getBody(), BuildingDTO.class);
                callback.accept(buildingDto);
            } else {
                handleApiError(response);
            }
        } catch (Exception e) {
            showError("Lỗi khi lấy dữ liệu chi tiết: " + e.getMessage());
        }
    }

    private void openEditWindow(BuildingDTO dto, Long buildingId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/BuildingEditView.fxml"));
            Parent root = loader.load();
            BuildingEditViewController controller = loader.getController();
            controller.setData(dto, buildingId);
            Stage stage = new Stage();
            stage.setTitle("Chỉnh sửa tòa nhà");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            showError("Lỗi khi mở cửa sổ sửa.");
        }
    }

    @FXML
    private void handleDeleteBuilding() {
        BuildingSearchResponse selected = buildingList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Chưa chọn tòa nhà nào để xóa!");
            return;
        }

        confirmDelete(selected.getId());
    }

    private void confirmDelete(Long buildingId) {
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmAlert.setTitle("Xác nhận xóa");
        confirmAlert.setHeaderText("Bạn có chắc chắn muốn xóa tòa nhà này không?");
        confirmAlert.setContentText("Thao tác này không thể hoàn tác.");

        confirmAlert.showAndWait().ifPresent(result -> {
            if (result.getText().equals("OK")) {
                deleteBuilding(buildingId);
            }
        });
    }

    private void deleteBuilding(Long buildingId) {
        String url = "http://localhost:8081/api/building/" + buildingId;
        try {
            HttpHeaders headers = createAuthHeaders();
            HttpEntity<Void> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                showSuccess("Đã xóa tòa nhà thành công!");
                fetchDataFromApi();
            } else {
                handleApiError(response);
            }
        } catch (Exception e) {
            showError("Lỗi khi xóa tòa nhà: " + e.getMessage());
        }
    }

    // Xử lý sự kiện nhấn nút "Thêm Tòa Nhà"
    @FXML
    public void handleAddBuilding() {
        try {
            // Tải FXML của cửa sổ thêm tòa nhà
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/BuildingAddView.fxml"));
            Parent root = loader.load();

            // Tạo một Stage mới cho cửa sổ Thêm Tòa Nhà
            Stage addBuildingStage = new Stage();
            addBuildingStage.setTitle("Thêm Tòa Nhà");
            addBuildingStage.setScene(new Scene(root));

            // Hiển thị cửa sổ
            addBuildingStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void showSuccess(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
