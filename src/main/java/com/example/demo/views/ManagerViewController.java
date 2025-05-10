package com.example.demo.views;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleLongProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import org.springframework.boot.autoconfigure.amqp.RabbitProperties.Stream;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import com.example.demo.model.dto.BuildingDTO;
import com.example.demo.model.dto.SectorDTO;
import com.example.demo.model.response.BuildingSearchResponse;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

public class ManagerViewController{

    @FXML
    private TextField txtname, txtward, txtdistrict, txtrank, txtareaFrom, txtareaTo, txtsellingPrice, txtnumberLivingFloor, txtnumberBasement;
    @FXML 
    private TextField txtMaPhanKhu, txtMaToaNha, txtTenToaNha, txtDiaChiToaNha, txtTenChuDauTuToaNha, txtGiaBanThapNhat, txtGiaBanCaoNhat, txtSoThangMay, txtSoTangO, txtSoTangHam, txtPhiOTo, txtPhiXeMay, txtTongDienTichToaNha, txtHangToaNha;
    @FXML
    private Button btnSearch, btnAdd, btnEdit, btnDelete, btnfet, btnAccMng, btnSuaPK, btnThemPK, btnHuyPK, btnSuaBuilding, btnThemBuilding, btnHuyBuilding, btnHuyCH, btnThemCH, btnSuaCH, btnHuyAcc, btnThemAcc, btnSuaAcc;

    @FXML
    private BorderPane paneApartManager, paneSectorManager, paneBuildingManager, paneUserManager;
    
    private Long selectedBuildingId;

    @FXML private TableView<BuildingDTO> buildingList;

    @FXML private TableColumn<BuildingDTO, Long> sectorId;
    @FXML private TableColumn<BuildingDTO, String> code, name, address, developerName, rank;
    @FXML private TableColumn<BuildingDTO, Double> totalArea;
    @FXML private TableColumn<BuildingDTO, Long> minSellingPrice, maxSellingPrice, bikeParkingMonthly, carParkingMonthly;
    @FXML private TableColumn<BuildingDTO, Integer> numberEle, numberLivingFloor, numberBasement;
    
    @FXML
    private GridPane PaneEditSector, PaneEditBuilding, PaneChangePwd, PaneEditApart, PaneEditUser;
    
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

    public void initialize() {
        setUpTableColumns();
    }
    
    private void setUpTableColumns() {
    	sectorId.setCellValueFactory(cellData -> {
            SectorDTO sector = cellData.getValue().getSector();
            Long sectorId = (sector != null) ? sector.getId() : null;
            return new ReadOnlyObjectWrapper<>(sectorId);
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

    @FXML
    private void fetchDataFromApi() {
        try {
            String url = "http://localhost:8081/api/building";
            HttpHeaders headers = createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

                List<BuildingDTO> buildings = objectMapper.readValue(
                    response.getBody(),
                    new TypeReference<>() {}
                );

                buildingList.setItems(FXCollections.observableArrayList(buildings));
                System.out.println("Đã tải " + buildings.size() + " tòa nhà.");
            } else {
                showError("Không thể lấy dữ liệu: " + response.getStatusCode());
            }
        } catch (Exception e) {
            showError("Lỗi khi gọi API: " + e.getMessage());
            e.printStackTrace();
        }
    }


    
    private HttpHeaders createAuthHeaders() {
        String auth = "nguyenvana:123456";
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
        	
        	System.out.println(query);
        	
            String url = "http://localhost:8081/api/building/search?" + query;
            HttpHeaders headers = createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
                List<BuildingDTO> buildings = objectMapper.readValue(
                    response.getBody(),
                    new TypeReference<List<BuildingDTO>>() {}
                );
                buildingList.setItems(FXCollections.observableArrayList(buildings));
            } else {
                showError("Không thể tìm kiếm dữ liệu: " + response.getStatusCode());
            }
        } catch (Exception e) {
            showError("Lỗi khi gọi API tìm kiếm: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String buildSearchQuery() {
        StringBuilder query = new StringBuilder();

        appendQueryParam(query, "name", txtname);
        appendQueryParam(query, "rank", txtrank);

        // Xử lý trường hợp address
        String address = buildAddress();
        if (!address.isBlank()) {
            query.append("&address=").append(address);  // Không encode
        }

        appendNumericQueryParam(query, "areaFrom", txtareaFrom);
        appendNumericQueryParam(query, "areaTo", txtareaTo);
        appendNumericQueryParam(query, "sellingPrice", txtsellingPrice);
        appendNumericQueryParam(query, "numberLivingFloor", txtnumberLivingFloor);
        appendNumericQueryParam(query, "numberBasement", txtnumberBasement);

        return query.toString();
    }


    private String buildAddress() {
        String ward = txtward.getText().trim();
        String district = txtdistrict.getText().trim();
        
        // Nếu cả 2 đều có giá trị
        if (!ward.isEmpty() && !district.isEmpty()) {
            return String.format("P. %s, Q. %s", ward, district);
        }

        // Nếu chỉ có 1 trong 2
        if (!ward.isEmpty()) {
            return "P. " + ward;
        }

        if (!district.isEmpty()) {
            return "Q. " + district;
        }

        // Nếu cả 2 đều không có giá trị
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
                showError(param + " phải là số hợp lệ!");
            }
        }
    }

    @FXML
    private void handleDeleteBuilding() {
        BuildingDTO selected = buildingList.getSelectionModel().getSelectedItem();
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
                loadBuildingList();
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
    
    @FXML
    public void handleAccMng() {
    	showOnly(paneUserManager);
    	hideEditPanes();
    }
    
    @FXML
    public void handleSectorMng() {
    	showOnly(paneSectorManager);
    	hideEditPanes();
    }
    
    @FXML
    public void handleBuildingMng() {
    	showOnly(paneBuildingManager);
    	fetchDataFromApi();
    	hideEditPanes();
    }
    
    @FXML
    public void handleApartMng() {
    	showOnly(paneApartManager);
    	hideEditPanes();
    }
    
    private void showOnly(Pane visiblePane) {
        List<Pane> panes = Arrays.asList(paneApartManager, paneSectorManager, paneBuildingManager, paneUserManager);
        for (Pane pane : panes) {
            boolean isVisible = pane == visiblePane;
            pane.setVisible(isVisible);
            pane.setManaged(isVisible);
        }
    }

    @FXML
    public void handleChangePassword() {
    	paneBuildingManager.setVisible(false);
    	paneBuildingManager.setManaged(false);
    	
    	paneSectorManager.setVisible(false);
    	paneSectorManager.setManaged(false);
    	
    	paneApartManager.setVisible(false);
    	paneApartManager.setManaged(false);
    	
    	paneUserManager.setVisible(false);
    	paneUserManager.setManaged(false);
    	
    	PaneChangePwd.setVisible(true);
        PaneChangePwd.setManaged(true);
    }
    
    @FXML
    public void handleDeleteSector() {
    	
    }
    
    @FXML
    public void handleBackSector() {
    	// Hiện pane chính
    	paneSectorManager.setVisible(true);
    	paneSectorManager.setManaged(true);
    	
        // Tắt Pane thêm mới
        PaneEditSector.setVisible(false);
        PaneEditSector.setManaged(false);
    }
    
    @FXML
    public void handleBackBuilding() {
    	// Hiện pane chính
    	paneBuildingManager.setVisible(true);
    	paneBuildingManager.setManaged(true);
    	
        // Tắt Pane thêm mới
        PaneEditBuilding.setVisible(false);
        PaneEditBuilding.setManaged(false);
    }
    
    @FXML
    public void handleBackBuilding_2() {
    	// Hiện pane chính
    	paneBuildingManager.setVisible(true);
    	paneBuildingManager.setManaged(true);
    	
        // Tắt Pane thêm mới
    	PaneChangePwd.setVisible(false);
    	PaneChangePwd.setManaged(false);
    }
    
    
    @FXML
    public void handleBackAcc() {
    	// Hiện pane chính
    	paneUserManager.setVisible(true);
    	paneUserManager.setManaged(true);
    	
        // Tắt Pane thêm mới
        PaneEditUser.setVisible(false);
        PaneEditUser.setManaged(false);
    }
    
    
    @FXML
    public void handleBackApart() {
    	// Hiện pane chính
    	paneApartManager.setVisible(true);
    	paneApartManager.setManaged(true);
    	
        // Tắt Pane thêm mới
        PaneEditApart.setVisible(false);
        PaneEditApart.setManaged(false);
    }
    
    @FXML
    public void handleAddSector() {
        // Ẩn Pane chính
    	paneSectorManager.setVisible(false);
    	paneSectorManager.setManaged(false);

        // Hiện Pane thêm mới
        PaneEditSector.setVisible(true);
        PaneEditSector.setManaged(true);

        // Chỉ hiện nút "Thêm"
        btnThemPK.setVisible(true);
        btnThemPK.setManaged(true);

        btnSuaPK.setVisible(false);
        btnSuaPK.setManaged(false);
    }
    
    @FXML
    public void handleEditSector() {
        // Ẩn Pane chính
    	paneSectorManager.setVisible(false);
    	paneSectorManager.setManaged(false);

        // Hiện Pane chỉnh sửa
        PaneEditSector.setVisible(true);
        PaneEditSector.setManaged(true);

        // Chỉ hiện nút "Sửa"
        btnSuaPK.setVisible(true);
        btnSuaPK.setManaged(true);

        btnThemPK.setVisible(false);
        btnThemPK.setManaged(false);
    }
    
    
    @FXML
    public void handleAddBuilding() {
        // Ẩn Pane chính
    	paneBuildingManager.setVisible(false);
    	paneBuildingManager.setManaged(false);

        // Hiện Pane thêm mới
        PaneEditBuilding.setVisible(true);
        PaneEditBuilding.setManaged(true);

        // Chỉ hiện nút "Thêm"
        btnThemBuilding.setVisible(true);
        btnThemBuilding.setManaged(true);

        btnSuaBuilding.setVisible(false);
        btnSuaBuilding.setManaged(false);
    }
    
    @FXML
    public void handleEdit() {
        // Lấy tòa nhà được chọn từ bảng
        BuildingDTO selectedBuilding = buildingList.getSelectionModel().getSelectedItem();

        // Kiểm tra nếu không có tòa nhà được chọn
        if (selectedBuilding == null) {
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle("Chưa chọn tòa nhà");
            alert.setHeaderText(null);
            alert.setContentText("Vui lòng chọn một tòa nhà trong bảng để chỉnh sửa.");
            alert.showAndWait();
            return;
        }

        // Lưu lại ID tòa nhà để gọi API PUT sau này
        selectedBuildingId = selectedBuilding.getId();

        // Chuyển sang form chỉnh sửa
        paneBuildingManager.setVisible(false);
        paneBuildingManager.setManaged(false);

        PaneEditBuilding.setVisible(true);
        PaneEditBuilding.setManaged(true);

        btnSuaBuilding.setVisible(true);
        btnSuaBuilding.setManaged(true);

        btnThemBuilding.setVisible(false);
        btnThemBuilding.setManaged(false);

        // Gán dữ liệu vào các TextField
        SectorDTO sector = selectedBuilding.getSector();
        txtMaPhanKhu.setText(sector != null ? String.valueOf(sector.getId()) : "");
        txtMaToaNha.setText(selectedBuilding.getCode());  // Gán mã tòa nhà từ selectedBuilding
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
    
    @FXML
    public void handleUpdateBuilding() {
        BuildingDTO buildingDTO = new BuildingDTO();

        try {
            buildingDTO.setCode(txtMaToaNha.getText());
            buildingDTO.setName(txtTenToaNha.getText());
            buildingDTO.setAddress(txtDiaChiToaNha.getText());
            buildingDTO.setDeveloperName(txtTenChuDauTuToaNha.getText());
            buildingDTO.setRank(txtHangToaNha.getText());
            buildingDTO.setTotalArea(parseDouble(txtTongDienTichToaNha.getText()));
            buildingDTO.setMinSellingPrice(parseLong(txtGiaBanThapNhat.getText()));
            buildingDTO.setMaxSellingPrice(parseLong(txtGiaBanCaoNhat.getText()));
            buildingDTO.setNumberEle(parseInteger(txtSoThangMay.getText()));
            buildingDTO.setNumberLivingFloor(parseInteger(txtSoTangO.getText()));
            buildingDTO.setNumberBasement(parseInteger(txtSoTangHam.getText()));
            buildingDTO.setBikeParkingMonthly(parseLong(txtPhiXeMay.getText()));
            buildingDTO.setCarParkingMonthly(parseLong(txtPhiOTo.getText()));

            Long sectorId = parseLong(txtMaPhanKhu.getText());
            SectorDTO sectorDTO = new SectorDTO();
            sectorDTO.setId(sectorId);
            buildingDTO.setSector(sectorDTO);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Dữ liệu không hợp lệ", "Vui lòng kiểm tra lại các trường nhập liệu.");
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            String jsonPayload = mapper.writeValueAsString(buildingDTO);

            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8081/api/building/" + selectedBuildingId;

            HttpHeaders headers = createAuthHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(jsonPayload, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                showAlert(Alert.AlertType.INFORMATION, "Cập nhật thành công", "Tòa nhà đã được cập nhật thành công!", null);
                loadBuildingList();

                // ✅ Quay lại giao diện quản lý tòa nhà
                paneBuildingManager.setVisible(true);
                paneBuildingManager.setManaged(true);
                PaneEditBuilding.setVisible(false);
                PaneEditBuilding.setManaged(false);
                
                clearAllTextFieldsIn(PaneEditBuilding);
            } else {
                handleApiError(response);
            }

        } catch (HttpClientErrorException | HttpServerErrorException httpEx) {
            String errorBody = httpEx.getResponseBodyAsString();
            System.err.println("Lỗi HTTP: " + httpEx.getStatusCode());
            System.err.println("Nội dung lỗi: " + errorBody);

            showAlert(Alert.AlertType.ERROR, "Cập nhật thất bại",
                      "Lỗi từ phía máy chủ: " + httpEx.getStatusCode(),
                      errorBody);
        } catch (RestClientException ex) {
            System.err.println("Lỗi RestClientException: " + ex.getMessage());
            showAlert(Alert.AlertType.ERROR, "Cập nhật thất bại",
                      "Lỗi không xác định khi kết nối tới API.",
                      ex.getMessage());
        } catch (Exception ex) {
            System.err.println("Lỗi không xác định: " + ex.getMessage());
            showAlert(Alert.AlertType.ERROR, "Cập nhật thất bại",
                      "Đã xảy ra lỗi không xác định.",
                      ex.getMessage());
        }
    }

    // Phương thức hỗ trợ hiển thị cảnh báo
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Các phương thức hỗ trợ chuyển đổi giá trị
    private Long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Giá trị không hợp lệ");
        }
    }

    private Integer parseInteger(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Giá trị không hợp lệ");
        }
    }

    private Double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Giá trị không hợp lệ");
        }
    }

    
    @FXML
    public void handleAddApart() {
        // Ẩn Pane chính
    	paneApartManager.setVisible(false);
    	paneApartManager.setManaged(false);

        // Hiện Pane thêm mới
    	PaneEditApart.setVisible(true);
    	PaneEditApart.setManaged(true);

        // Chỉ hiện nút "Thêm"
        btnThemCH.setVisible(true);
        btnThemCH.setManaged(true);

        btnSuaCH.setVisible(false);
        btnSuaCH.setManaged(false);
    }
    
    @FXML
    public void handleEditApart() {
        // Ẩn Pane chính
    	paneApartManager.setVisible(false);
    	paneApartManager.setManaged(false);

        // Hiện Pane chỉnh sửa
    	PaneEditApart.setVisible(true);
    	PaneEditApart.setManaged(true);

        // Chỉ hiện nút "Sửa"
    	btnSuaCH.setVisible(true);
    	btnSuaCH.setManaged(true);

    	btnThemCH.setVisible(false);
    	btnThemCH.setManaged(false);
    }
    
    @FXML
    public void handleThemUser() {
        // Ẩn Pane chính
    	paneUserManager.setVisible(false);
    	paneUserManager.setManaged(false);

        // Hiện Pane thêm mới
    	PaneEditUser.setVisible(true);
    	PaneEditUser.setManaged(true);

        // Chỉ hiện nút "Thêm"
        btnThemAcc.setVisible(true);
        btnThemAcc.setManaged(true);

        btnSuaAcc.setVisible(false);
        btnSuaAcc.setManaged(false);
    }
    
    @FXML
    public void handleSuaUser() {
        // Ẩn Pane chính
    	paneUserManager.setVisible(false);
    	paneUserManager.setManaged(false);

        // Hiện Pane chỉnh sửa
    	PaneEditUser.setVisible(true);
    	PaneEditUser.setManaged(true);

        // Chỉ hiện nút "Sửa"
    	btnSuaAcc.setVisible(true);
    	btnSuaAcc.setManaged(true);

    	btnThemAcc.setVisible(false);
    	btnThemAcc.setManaged(false);
    }
    
    private void loadBuildingList() {
        String url = "http://localhost:8081/api/building";

        try {
            HttpHeaders headers = createAuthHeaders(); // tạo headers có Authorization nếu cần
            HttpEntity<String> entity = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

                List<BuildingDTO> buildingListData = objectMapper.readValue(
                    response.getBody(),
                    new TypeReference<>() {} // Java 11+ hỗ trợ diamond operator
                );

                buildingList.setItems(FXCollections.observableArrayList(buildingListData));
            } else {
                showError("Không thể tải danh sách: " + response.getStatusCode());
            }
        } catch (Exception e) {
            showError("Lỗi khi tải danh sách tòa nhà: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleAddBuilding_2() {
        BuildingDTO buildingDTO = new BuildingDTO();

        try {
            buildingDTO.setCode(txtMaToaNha.getText());
            buildingDTO.setName(txtTenToaNha.getText());
            buildingDTO.setAddress(txtDiaChiToaNha.getText());
            buildingDTO.setDeveloperName(txtTenChuDauTuToaNha.getText());
            buildingDTO.setRank(txtHangToaNha.getText());
            buildingDTO.setTotalArea(parseDouble(txtTongDienTichToaNha.getText()));
            buildingDTO.setMinSellingPrice(parseLong(txtGiaBanThapNhat.getText()));
            buildingDTO.setMaxSellingPrice(parseLong(txtGiaBanCaoNhat.getText()));
            buildingDTO.setNumberEle(parseInteger(txtSoThangMay.getText()));
            buildingDTO.setNumberLivingFloor(parseInteger(txtSoTangO.getText()));
            buildingDTO.setNumberBasement(parseInteger(txtSoTangHam.getText()));
            buildingDTO.setBikeParkingMonthly(parseLong(txtPhiXeMay.getText()));
            buildingDTO.setCarParkingMonthly(parseLong(txtPhiOTo.getText()));

            Long sectorId = parseLong(txtMaPhanKhu.getText());
            SectorDTO sectorDTO = new SectorDTO();
            sectorDTO.setId(sectorId);
            buildingDTO.setSector(sectorDTO);

        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu", "Thông tin không hợp lệ", "Vui lòng kiểm tra lại các trường số.");
            return;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            String jsonPayload = mapper.writeValueAsString(buildingDTO);

            RestTemplate restTemplate = new RestTemplate();
            String url = "http://localhost:8081/api/building";
            HttpHeaders headers = createAuthHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(jsonPayload, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                showAlert(Alert.AlertType.INFORMATION, "Thành công", "Đã thêm tòa nhà mới!", null);
                loadBuildingList();

                // ✅ Quay lại giao diện quản lý tòa nhà
                paneBuildingManager.setVisible(true);
                paneBuildingManager.setManaged(true);
                PaneEditBuilding.setVisible(false);
                PaneEditBuilding.setManaged(false);
                
                clearAllTextFieldsIn(PaneEditBuilding);
            } else {
                handleApiError(response);
            }

        } catch (Exception e) {
            showError("Lỗi kết nối hoặc xử lý: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void clearTextFieldsInPane(Pane pane) {
        for (Node node : pane.getChildrenUnmodifiable()) {
            if (node instanceof TextField) {
                ((TextField) node).clear(); // hoặc setText(null)
            } else if (node instanceof Pane) {
                // Đệ quy nếu trong Pane có chứa Pane con
                clearTextFieldsInPane((Pane) node);
            }
        }
    }

    private void clearAllTextFieldsIn(Pane rootPane) {
        for (Node node : rootPane.getChildrenUnmodifiable()) {
            if (node instanceof TextField) {
                ((TextField) node).clear();
            } else if (node instanceof Pane) {
                clearAllTextFieldsIn((Pane) node); // Đệ quy cho các Pane lồng nhau
            }
        }
    }
    
    private void hideEditPanes() {
        // Ẩn tất cả các pane chỉnh sửa
        PaneEditSector.setVisible(false);
        PaneEditSector.setManaged(false);

        PaneEditBuilding.setVisible(false);
        PaneEditBuilding.setManaged(false);

        PaneChangePwd.setVisible(false);
        PaneChangePwd.setManaged(false);

        PaneEditApart.setVisible(false);
        PaneEditApart.setManaged(false);

        PaneEditUser.setVisible(false);
        PaneEditUser.setManaged(false);
    }

    
    private void showSuccess(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
