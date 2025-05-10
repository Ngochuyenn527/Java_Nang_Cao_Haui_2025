package com.example.demo.views;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import com.example.demo.entity.ApartmentEntity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.example.demo.model.dto.BuildingDTO;
import com.example.demo.model.response.BuildingSearchResponse;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;


@Component
public class ManagerViewController {

    @FXML
    private TextField txtname, txtaddress, txtrank, txtareaFrom, txtareaTo, txtsellingPrice, txtnumberLivingFloor, txtnumberBasement;
    @FXML
    private Button btnSearch, btnAdd, btnEdit, btnDelete, btnfet, btnAccMng;
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
    @FXML
    private BorderPane paneApartManager, paneSectorManager, paneBuildingManager, paneUserManager;
    // <------- Quản lý căn hộ -------> \\
    @FXML private TableView<ApartmentEntity> buildingApartList;

    @FXML private TableColumn<ApartmentEntity, String> colCode;
    @FXML private TableColumn<ApartmentEntity, String> colName;
    @FXML private TableColumn<ApartmentEntity, Integer> colFloor;
    @FXML private TableColumn<ApartmentEntity, Double> colArea;
    @FXML private TableColumn<ApartmentEntity, Integer> colBedrooms;
    @FXML private TableColumn<ApartmentEntity, Integer> colBathrooms;
    @FXML private TableColumn<ApartmentEntity, Long> colPrice;
    @FXML private TableColumn<ApartmentEntity, String> colStatus;
    @FXML private TableColumn<ApartmentEntity, String> colFacing;
    @FXML private TableColumn<ApartmentEntity, String> colDescription;
    @FXML private TableColumn<ApartmentEntity, Integer> colElectricity;
    @FXML private TableColumn<ApartmentEntity, Integer> colWater;
    @FXML private TableColumn<ApartmentEntity, Double> colCeiling;

    @FXML private TextField txtBuildingId;
    @FXML private TextField txtApartmentId;
    @FXML private TextField txtApartmentName;
    @FXML private TextField txtFloor;
    @FXML private TextField txtArea;
    @FXML private TextField txtBedrooms;
    @FXML private TextField txtBathrooms;
    @FXML private TextField txtPrice;
    @FXML private ComboBox<String> cbStatus;
    @FXML private TextField txtDirection;
    @FXML private TextField txtDescription;
    @FXML private TextField txtView;
    @FXML private TextField txtElectricityFee;
    @FXML private TextField txtWaterFee;
    @FXML private TextField txtCeilingHeight;
    public void setApartmentData(ApartmentEntity apartment) {

        txtBuildingId.setText (String.valueOf(apartment.getBuilding().getId()));
        txtApartmentId.setText(apartment.getId().toString());
        txtApartmentName.setText(apartment.getName());
        txtFloor.setText(String.valueOf(apartment.getFloor()));
        txtArea.setText(String.valueOf(apartment.getArea()));
        txtBedrooms.setText(String.valueOf(apartment.getNumberOfBedrooms()));
        txtBathrooms.setText(String.valueOf(apartment.getNumberOfBathrooms()));
        txtPrice.setText(String.valueOf(apartment.getPrice()));
        cbStatus.setValue(apartment.getStatus());
        txtDirection.setText(apartment.getDescription());
        txtDescription.setText(apartment.getDescription());
        txtView.setText(apartment.getView());
        txtElectricityFee.setText(String.valueOf(apartment.getElectricityPricePerKwh()));
        txtWaterFee.setText(String.valueOf(apartment.getWaterPricePerM3()));
        txtCeilingHeight.setText(String.valueOf(apartment.getCeilingHeight()));
    }
    @FXML
    private Button btnEditApart;

    @FXML
    private TextField txtSearchByFloor;

    @FXML
    private ComboBox<String> cbbSearchByStatus;

    @FXML
    private Button btnCancel; // đảm bảo nút này được khai báo đúng


    @FXML
    private void cancel() {
        // Lấy stage hiện tại từ nút và đóng nó
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    // Phương thức lọc căn hộ theo trạng thái
    @FXML
    private void handleSearchByStatus() throws Exception {
        String selectedStatus = cbbSearchByStatus.getValue();

        ObservableList<ApartmentEntity> filteredList = FXCollections.observableArrayList();

        if (selectedStatus != null && !selectedStatus.equals("Tất cả")) {
            // Lọc danh sách căn hộ theo trạng thái
            filteredList.addAll(apartmentViewController.fetchAllApartments().stream()
                    .filter(a -> selectedStatus.equals(a.getStatus()))
                    .collect(Collectors.toList()));
        } else {
            // Nếu chọn "Tất cả", hiển thị tất cả căn hộ
            filteredList.addAll(apartmentViewController.fetchAllApartments());
        }

        // Cập nhật danh sách căn hộ trên giao diện
        buildingApartList.setItems(filteredList);
    }

    @FXML
    private void handleSearchByFloor() {
        String floorText = txtSearchByFloor.getText().trim();

        // Kiểm tra nếu giá trị nhập vào là số hợp lệ
        ObservableList<ApartmentEntity> filteredList = FXCollections.observableArrayList();

        try {
            // Kiểm tra xem có nhập giá trị vào không và có phải số không
            if (!floorText.isEmpty()) {
                Integer floor = Integer.parseInt(floorText);

                // Lọc danh sách căn hộ theo tầng
                filteredList.addAll(apartmentViewController.fetchAllApartments().stream()
                        .filter(a -> a.getFloor() != null && a.getFloor().equals(floor))
                        .collect(Collectors.toList()));
            } else {
                // Nếu không có giá trị nhập vào, hiển thị tất cả căn hộ
                filteredList.addAll(apartmentViewController.fetchAllApartments());
            }

        } catch (Exception e) {
            // Nếu không phải số, in ra lỗi
            e.printStackTrace();
        }

        // Cập nhật danh sách căn hộ trên giao diện
        buildingApartList.setItems(filteredList);
    }
    @FXML
    private void handleRefreshTable() throws Exception {
        // logic xử lý làm mới bảng
        ObservableList<ApartmentEntity> list = apartmentViewController.fetchAllApartments();

        // In thử ra console để xác minh
        list.forEach(apartment -> System.out.println(apartment.getCode() + " - " + apartment.getName()));

        buildingApartList.setItems(list);
    }

    @FXML
    private void handleEditApartButton() throws Exception {
        System.out.println("bat dau handleEditApartButton");
        ApartmentEntity selectedApartment = buildingApartList.getSelectionModel().getSelectedItem();
        System.out.println(selectedApartment);
        if (selectedApartment != null) {
            showEditForm(selectedApartment);
        } else {
            // Thông báo nếu chưa chọn hàng nào
            System.out.println("Chưa chọn dòng để sửa!");
        }
    }

    private void showEditForm(ApartmentEntity apartment) {
        try {
            System.out.println("bat dau showEditForm");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/EditApartmentView.fxml"));
            Parent root = loader.load();


            EditApartmentController controller = loader.getController();
            if (controller == null) {
                throw new IllegalStateException("EditApartmentController is null. Check FXML configuration.");
            }

            // Truyền dữ liệu sang controller
            controller.setApartmentData(apartment);

            // Hiển thị form
            Stage stage = new Stage();
            stage.setTitle("Sửa căn hộ");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final RestTemplate restTemplate = new RestTemplate();

//    @Autowired
//    private ApartmentConverter apartmentConverter;
//    @Autowired
//    private ApartmentService apartmentService;
    // <--- sửa căn hộ ----> \\

    @Autowired
    private ApplicationContext applicationContext;
    @FXML
    private Button btnSave;
    private ApartmentViewController apartmentViewController = new ApartmentViewController();

    public void initialize() {
        try {
            if (apartmentViewController == null) {
                System.out.println("ApartmentConverter is null!");
            } else {
                System.out.println("ApartmentConverter is initialized: " + apartmentViewController);
            }
            // Ánh xạ các thuộc tính trong ApartmentEntity tới từng cột TableView

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

            // Thêm các trạng thái vào ComboBox
            ObservableList<String> statuses = FXCollections.observableArrayList(
                    "Tất cả",      // Nếu bạn muốn có tùy chọn không lọc
                    "Available",
                    "Sold");
            cbbSearchByStatus.setItems(statuses);

            // Chọn mặc định nếu cần
            cbbSearchByStatus.getSelectionModel().selectFirst();

            // Gọi API và set dữ liệu
            ObservableList<ApartmentEntity> list = apartmentViewController.fetchAllApartments();

            // In thử ra console để xác minh
            list.forEach(apartment -> System.out.println(apartment.getCode() + " - " + apartment.getName()));

            buildingApartList.setItems(list);
            txtSearchByFloor.setOnKeyReleased(event -> handleSearchByFloor());
            // Lọc dữ liệu khi trạng thái được thay đổi
            cbbSearchByStatus.setOnAction(event -> {
                try {
                    handleSearchByStatus();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
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
    
    @FXML
    public void handleAccMng() {
    	showOnly(paneUserManager);
    }
    
    @FXML
    public void handleSectorMng() {
    	showOnly(paneSectorManager);
    }
    
    @FXML
    public void handleBuildingMng() {
    	showOnly(paneBuildingManager);
    }
    
    @FXML
    public void handleApartMng() {
    	showOnly(paneApartManager);
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
        
    }
    
    @FXML
    public void handleEditApart() {
        
    }

    @FXML
    public void handleAddApart() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/FXML/AddApartmentView.fxml"));
        Parent root = loader.load();

        AddApartmentController controller = loader.getController();
        if (controller == null) {
            throw new IllegalStateException("AddApartmentController is null. Check FXML configuration.");
        }

        // Hiển thị cửa sổ mới
        Stage stage = new Stage();
        stage.setTitle("Thêm căn hộ");
        stage.setScene(new Scene(root));
        stage.show();
    }


    private void showSuccess(String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
