package com.example.demo.views;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import com.example.demo.model.dto.ApartmentDTO;
import com.example.demo.model.dto.BuildingDTO;
import com.example.demo.model.dto.SectorDTO;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.service.BuildingService;
import com.example.demo.service.SectorService;
import com.example.demo.views.CommonUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

@Component
public class ManagerViewController {
	
    @Autowired
    private BuildingService buildingService;
    
    @Autowired
    private SectorService sectorService;

	@FXML private Label txtNameMng, totalProjectsLabel, totalDistrictsLabel, avgPriceLabel, TotalSectorLabel;
    @FXML private TextField txtname, txtward, txtdistrict, txtrank, txtareaFrom, txtareaTo, txtsellingPrice, txtnumberLivingFloor, txtnumberBasement;
    @FXML private TextField txtMaPhanKhu_building, txtMaToaNha, txtTenToaNha, txtDiaChiToaNha, txtTenChuDauTuToaNha, txtGiaBanThapNhat, txtGiaBanCaoNhat, txtSoThangMay, txtSoTangO, txtSoTangHam, txtPhiOTo, txtPhiXeMay, txtTongDienTichToaNha, txtHangToaNha;
    @FXML private TextField txtMaPhanKhu, txtTenPhanKhu,txtPosPk, txtViTri, txtTongDienTich, txtMoTa, txtTenQuanLy, txtSoTangCaoNhat;
    @FXML private TextField txtMaToaNha_CH, txtMaCanHo, txtTenCanHo, TxtSoTangCH, txtDienTichCH, txtSoPhongNgu, txtSoPhongTam, txtGiaCH, txtHuongCH, txtMoTaCH, txtViewCH, txtTienDienCH, txtTienNuocCH, txtChieuCaoCH, txtSearchByFloor;
    @FXML private DatePicker datePickerKhoiCong, datePickerHoanThanh;
    @FXML private TextField txtTenNguoiDung, txtHoTen, txtSoDienThoai, txtEmail, txtSearchUsername;
    @FXML private Button btnSearch, btnAdd, btnEdit, btnDelete, btnfet, btnAccMng, btnSuaPK, btnThemPK, btnHuyPK, btnSuaBuilding, btnThemBuilding, btnHuyBuilding, btnHuyCH, btnThemCH, btnSuaCH, btnHuyAcc, btnThemAcc, btnSuaAcc, btnStatistical;
    @FXML private BorderPane paneApartManager, paneSectorManager, paneBuildingManager, paneUserManager;
    @FXML private TableView<BuildingDTO> buildingList;
    @FXML private TableView<ApartmentDTO> ApartList;
    @FXML private TableColumn<BuildingDTO, Long> sectorId;
    @FXML private TableColumn<BuildingDTO, String> code, name, address, developerName, rank;
    @FXML private TableColumn<BuildingDTO, Double> totalArea;
    @FXML private TableColumn<BuildingDTO, Long> minSellingPrice, maxSellingPrice, bikeParkingMonthly, carParkingMonthly;
    @FXML private TableColumn<BuildingDTO, Integer> numberEle, numberLivingFloor, numberBasement;
    @FXML private TableColumn<ApartmentDTO, String> colCode, colName, colStatus, colFacing, colDescription;
    @FXML private TableColumn<ApartmentDTO, Integer> colFloor, colBedrooms, colBathrooms;
    @FXML private TableColumn<ApartmentDTO, Double> colArea, colPrice, colElectricity, colWater, colCeiling;
    @FXML private GridPane PaneEditSector, PaneEditBuilding, PaneChangePwd, PaneEditApart, PaneEditUser, statisticalPane;
    @FXML private TableView<SectorDTO> SectorList;
    @FXML private TableColumn<SectorDTO, String> MaPK, TenPK, ViTriPK, MoTaPK, QuanLyPK, TrangThaiPK, StartDatePK, EndDatePk;
    @FXML private TableColumn<SectorDTO, Double> TongDTPK;
    @FXML private TableColumn<SectorDTO, Integer> SoTangPK;
    @FXML private ComboBox<String> comboTrangThai, cbbSearchStatusPK, cbbStatusUser, cbbSearchRole, cbbTrangThaiCH, cbbSearchByStatus;
    @FXML private TableView<UserDTO> userList;
    @FXML private TableColumn<UserDTO, String> nameUser, fullNameUser, SdtUser, EmailUser, RoleUser;
    @FXML
    private PieChart pieChart;
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private LineChart<String, Number> sectorLineChart;

    private final BuildingManagerView buildingManager = new BuildingManagerView();
    private final SectorManagerView sectorManager = new SectorManagerView();
    private final UserManagerView userManager = new UserManagerView();
    private ApartmentManagerView apartmentView = new ApartmentManagerView();
    public void initialize() {
        buildingManager.setUpTableColumns(buildingList, sectorId, code, name, address, developerName, rank, totalArea,
                minSellingPrice, maxSellingPrice, numberEle, numberLivingFloor, numberBasement, bikeParkingMonthly, carParkingMonthly);
        sectorManager.setUpSectorTableColumns(MaPK, TenPK, ViTriPK, TongDTPK, SoTangPK, MoTaPK, QuanLyPK, TrangThaiPK, StartDatePK, EndDatePk);
        userManager.setUpTableColumns(nameUser, fullNameUser, SdtUser, EmailUser, RoleUser);
        apartmentView.setUpTableColumns(
                ApartList, colCode, colName, colFloor, colArea, colBedrooms,
                colBathrooms, colPrice, colStatus, colFacing, colDescription,
                colElectricity, colWater, colCeiling
        );
        
        setUpComboBoxes();
        // Tổng số dự án
        totalProjectsLabel.setText(String.valueOf(buildingService.getDistinctProjectCount()));
        
        // Tổng số quận
        totalDistrictsLabel.setText(String.valueOf(buildingService.getDistinctAddress()));
        
        // Giá trung bình
        avgPriceLabel.setText(String.valueOf(buildingService.getDistinctAverage()));
        
        TotalSectorLabel.setText(String.valueOf(sectorService.getSectorCount()));
        
        // Biểu đồ số lượng dự án trong quận, xã
        loadPieChartData();
        
        updateSectorLineChart();
        
    }

    private void setUpComboBoxes() {
        ObservableList<String> statuses = FXCollections.observableArrayList(
                "Đang triển khai", "Chờ duyệt", "Hoàn thành", "Đang thi công", "Đã phê duyệt"
        );
        ObservableList<String> roles = FXCollections.observableArrayList(
                "STAFF", "MANAGER"
        );
        ObservableList<String> apartmentStatuses = FXCollections.observableArrayList(
                "Available", "Sold", "Reserved"
        );
        comboTrangThai.setItems(statuses);
        cbbSearchStatusPK.setItems(statuses);
        cbbStatusUser.setItems(roles);
        cbbSearchRole.setItems(roles);
        cbbTrangThaiCH.setItems(apartmentStatuses);
        cbbSearchByStatus.setItems(apartmentStatuses);
    }

    @FXML
    private void fetchDataFromApi() {
        buildingManager.fetchDataFromApi(buildingList);
    }

    @FXML
    private void fetchSectorDataFromApi() {
        sectorManager.fetchSectorDataFromApi(SectorList);
    }

    @FXML
    private void fetchUserDataFromApi() {
        userManager.fetchUserDataFromApi(userList);
    }

    @FXML
    private void handleSearch() {
        buildingManager.handleSearch(buildingList, txtname, txtrank, txtward, txtdistrict, txtareaFrom, txtareaTo,
                txtsellingPrice, txtnumberLivingFloor, txtnumberBasement);
    }

    @FXML
    private void handleSearchByUsername() {
        userManager.handleSearchByUsername(userList, txtSearchUsername);
    }

    @FXML
    private void handleSearchByRole() {
        userManager.handleSearchByRole(userList, cbbSearchRole);
    }

    @FXML
    private void handleDeleteBuilding() {
        buildingManager.handleDeleteBuilding(buildingList);
    }

    @FXML
    private void handleDeleteUser() {
        userManager.handleDeleteUser(userList);
    }

    @FXML
    public void handleAccMng() {
        userManager.handleUserMng(paneUserManager, paneApartManager, paneSectorManager, paneBuildingManager, userList,
                PaneEditSector, PaneEditBuilding, PaneChangePwd, PaneEditApart, PaneEditUser);
        txtNameMng.setText("Quản Lý Tài Khoản");
    }
    
    @FXML
    public void handleStatistical() {
    	CommonUtils.showOnly(statisticalPane, paneUserManager, paneApartManager, paneSectorManager, paneBuildingManager,
                PaneEditSector, PaneEditBuilding, PaneChangePwd, PaneEditApart, PaneEditUser);
    }

    @FXML
    public void handleSectorMng() {
        sectorManager.handleSectorMng(paneSectorManager, paneApartManager, paneBuildingManager, paneUserManager, SectorList,
                PaneEditSector, PaneEditBuilding, PaneChangePwd, PaneEditApart, PaneEditUser, statisticalPane);
        txtNameMng.setText("Quản Lý Phân Khu");
    }

    @FXML
    public void handleBuildingMng() {
        buildingManager.handleBuildingMng(paneBuildingManager, paneApartManager, paneSectorManager, paneUserManager, buildingList,
                PaneEditSector, PaneEditBuilding, PaneChangePwd, PaneEditApart, PaneEditUser, statisticalPane);
        txtNameMng.setText("Quản Lý Tòa Nhà");
    }

    @FXML
    public void handleApartMng() {
        apartmentView.handleApartMng(paneApartManager, paneBuildingManager, paneSectorManager, paneUserManager, ApartList,
                PaneEditSector, PaneEditBuilding, PaneChangePwd, PaneEditApart, PaneEditUser, statisticalPane);
        txtNameMng.setText("Quản Lý Căn Hộ");
    }

    @FXML
    public void handleChangePassword() {
        CommonUtils.showOnly(PaneChangePwd, paneApartManager, paneSectorManager, paneBuildingManager, paneUserManager,
        		PaneEditSector, PaneEditBuilding, PaneEditApart, PaneEditUser, statisticalPane);
        txtNameMng.setText("Đổi Mật Khẩu");
    }

    @FXML
    public void handleBackSector() {
        sectorManager.backToSectorManager(paneSectorManager, PaneEditSector);
        txtNameMng.setText("Quản Lý Phân Khu");
    }

    @FXML
    public void handleBackBuilding() {
        buildingManager.handleBackBuilding(paneBuildingManager, PaneEditBuilding);
        txtNameMng.setText("Quản Lý Tòa Nhà");
    }

    @FXML
    public void handleBackBuilding_2() {
        CommonUtils.hideEditPanes(PaneChangePwd);
    }

    @FXML
    public void handleBackAcc() {
        userManager.backToUserManager(paneUserManager, PaneEditUser);
        txtNameMng.setText("Quản Lý Tài Khoản");
    }

    @FXML
    public void handleBackApart() {
        apartmentView.backToApartManager(paneApartManager, PaneEditApart);
        txtNameMng.setText("Quản Lý Căn Hộ");
    }

    @FXML
    public void handleAddSector() {
        sectorManager.handleAddSector(paneSectorManager, PaneEditSector);      
        CommonUtils.showAddBtn(btnSuaPK, btnThemPK);
        txtNameMng.setText("Thêm Phân Khu");
    }

    @FXML
    public void handleAddBuilding() {
        buildingManager.handleAddBuilding(paneBuildingManager, PaneEditBuilding);
        CommonUtils.showAddBtn(btnSuaBuilding, btnThemBuilding);
        txtNameMng.setText("Thêm Tòa Nhà");
    }

    @FXML
    public void handleEdit() {
        buildingManager.handleEdit(buildingList, paneBuildingManager, PaneEditBuilding, txtMaPhanKhu_building, txtMaToaNha,
                txtTenToaNha, txtDiaChiToaNha, txtTenChuDauTuToaNha, txtGiaBanThapNhat, txtGiaBanCaoNhat,
                txtSoThangMay, txtSoTangO, txtSoTangHam, txtHangToaNha, txtTongDienTichToaNha, txtPhiXeMay, txtPhiOTo);
        CommonUtils.showEditBtn(btnSuaBuilding, btnThemBuilding);
        txtNameMng.setText("Sửa Tòa Nhà");
    }

    @FXML
    public void handleUpdateBuilding() {
        buildingManager.handleUpdateBuilding(paneBuildingManager, PaneEditBuilding, buildingList, txtMaPhanKhu_building, txtMaToaNha,
                txtTenToaNha, txtDiaChiToaNha, txtTenChuDauTuToaNha, txtGiaBanThapNhat, txtGiaBanCaoNhat,
                txtSoThangMay, txtSoTangO, txtSoTangHam, txtHangToaNha, txtTongDienTichToaNha, txtPhiXeMay, txtPhiOTo);
        txtNameMng.setText("Quản Lý Tòa Nhà");
    }

    @FXML
    public void handleAddBuilding_2() {
        buildingManager.handleAddBuilding_2(paneBuildingManager, PaneEditBuilding, buildingList, txtMaPhanKhu_building, txtMaToaNha,
                txtTenToaNha, txtDiaChiToaNha, txtTenChuDauTuToaNha, txtGiaBanThapNhat, txtGiaBanCaoNhat,
                txtSoThangMay, txtSoTangO, txtSoTangHam, txtHangToaNha, txtTongDienTichToaNha, txtPhiXeMay, txtPhiOTo);
        txtNameMng.setText("Quản Lý Tòa Nhà");
    }

    @FXML
    public void handleAddApart() {
        CommonUtils.showOnly(PaneEditApart, paneApartManager, paneSectorManager, paneBuildingManager, paneUserManager);      
        CommonUtils.showAddBtn(btnSuaCH, btnThemCH);
        txtNameMng.setText("Thêm Căn Hộ");
    }

    @FXML
    public void handleEditApart() {
        CommonUtils.showOnly(PaneEditApart, paneApartManager, paneSectorManager, paneBuildingManager, paneUserManager);
        CommonUtils.showEditBtn(btnSuaCH, btnThemCH);
        txtNameMng.setText("Sửa Căn Hộ");
    }

    @FXML
    public void handleThemUser() {
        userManager.handleAddUser(paneUserManager, PaneEditUser);
        CommonUtils.showAddBtn(btnSuaAcc, btnThemAcc);
        txtNameMng.setText("Thêm Tài Khoản");
    }

    @FXML
    public void handleSuaUser() {
        userManager.handleEditUser(userList, paneUserManager, PaneEditUser, txtTenNguoiDung, txtHoTen, txtSoDienThoai,
                txtEmail, cbbStatusUser);
        CommonUtils.showEditBtn(btnSuaAcc, btnThemAcc);
        txtNameMng.setText("Sửa Tài Khoản");
    }

    @FXML
    public void handleAddUser_2() {
        userManager.handleAddUser_2(paneUserManager, PaneEditUser, userList, txtTenNguoiDung, txtHoTen, txtSoDienThoai,
                txtEmail, cbbStatusUser);
        txtNameMng.setText("Quản Lý Tài Khoản");
    }

    @FXML
    public void handleUpdateUser() {
        userManager.handleUpdateUser(paneUserManager, PaneEditUser, userList, txtTenNguoiDung, txtHoTen, txtSoDienThoai,
                txtEmail, cbbStatusUser);
        txtNameMng.setText("Quản Lý Tài Khoản");
    }

    @FXML
    public void handleAddSector_2() {
        sectorManager.handleAddSector_2(paneSectorManager, PaneEditSector, SectorList, txtMaPhanKhu, txtTenPhanKhu,
                txtViTri, txtTongDienTich, txtMoTa, txtTenQuanLy, txtSoTangCaoNhat, datePickerKhoiCong, datePickerHoanThanh,
                comboTrangThai);
        txtNameMng.setText("Quản Lý Phân Khu");
    }

    @FXML
    public void handleUpdateSector() {
        sectorManager.handleUpdateSector(paneSectorManager, PaneEditSector, SectorList, txtMaPhanKhu, txtTenPhanKhu,
                txtViTri, txtTongDienTich, txtMoTa, txtTenQuanLy, txtSoTangCaoNhat, datePickerKhoiCong, datePickerHoanThanh,
                comboTrangThai);
        txtNameMng.setText("Quản Lý Phân Khu");
    }

    @FXML
    public void handleEditSector() {
        sectorManager.handleEditSector(SectorList, paneSectorManager, PaneEditSector, txtMaPhanKhu, txtTenPhanKhu,
                txtViTri, txtTongDienTich, txtMoTa, txtTenQuanLy, txtSoTangCaoNhat, datePickerKhoiCong, datePickerHoanThanh,
                comboTrangThai);
        CommonUtils.showEditBtn(btnSuaPK, btnThemPK);
        txtNameMng.setText("Sửa Phân Khu");
    }

    @FXML
    public void handleDeleteSector() {
        sectorManager.handleDeleteSector(SectorList);
    }
    
    @FXML
    public void handleSearchByPos() {
        sectorManager.handleSearchByLocation(SectorList, txtPosPk);
    }

    @FXML
    public void handleSearchByStatusPK() {
        sectorManager.handleSearchByStatus(SectorList, cbbSearchStatusPK);
    }
    
    @FXML
    private void handleThemCH() {
        apartmentView.handleAddApart(paneApartManager, PaneEditApart);
        CommonUtils.showAddBtn(btnSuaCH, btnThemCH);
        txtNameMng.setText("Thêm Căn Hộ");
    }

    @FXML
    private void handleSuaCH() {
        apartmentView.handleEditApart(ApartList, paneApartManager, PaneEditApart,
                txtMaToaNha_CH, txtMaCanHo, txtTenCanHo, TxtSoTangCH, txtDienTichCH, txtSoPhongNgu,
                txtSoPhongTam, txtGiaCH, cbbTrangThaiCH, txtHuongCH, txtMoTaCH, txtViewCH,
                txtTienDienCH, txtTienNuocCH, txtChieuCaoCH);
        CommonUtils.showEditBtn(btnSuaCH, btnThemCH);
        txtNameMng.setText("Sửa Căn Hộ");
    }

    @FXML
    private void handleAddApart_2() {
        apartmentView.handleAddApart_2(paneApartManager, PaneEditApart, ApartList,
                txtMaToaNha_CH, txtMaCanHo, txtTenCanHo, TxtSoTangCH, txtDienTichCH, txtSoPhongNgu,
                txtSoPhongTam, txtGiaCH, cbbTrangThaiCH, txtHuongCH, txtMoTaCH, txtViewCH,
                txtTienDienCH, txtTienNuocCH, txtChieuCaoCH);
        txtNameMng.setText("Quản Lý Căn Hộ");
    }

    @FXML
    private void handleUpdateApart() {
        apartmentView.handleUpdateApart(paneApartManager, PaneEditApart, ApartList,
                txtMaToaNha_CH, txtMaCanHo, txtTenCanHo, TxtSoTangCH, txtDienTichCH, txtSoPhongNgu,
                txtSoPhongTam, txtGiaCH, cbbTrangThaiCH, txtHuongCH, txtMoTaCH, txtViewCH,
                txtTienDienCH, txtTienNuocCH, txtChieuCaoCH);
        txtNameMng.setText("Quản Lý Căn Hộ");
    }

    @FXML
    private void handleDeleteApart() {
        apartmentView.handleDeleteApart(ApartList);
    }

    @FXML
    private void handleSearchByStatus() {
        apartmentView.handleSearchByStatus(ApartList, cbbSearchByStatus);
    }

    @FXML
    private void handleSearchbyFloor() {
        apartmentView.handleSearchByFloor(ApartList, txtSearchByFloor);
    }

    private void loadPieChartData() {
        pieChart.getData().clear();
        var pieChartData = buildingService.getPieChartDataForProjectsByDistrict();
        if (pieChartData.isEmpty()) {
            System.out.println("Không có dữ liệu để hiển thị trên PieChart");
            pieChart.setTitle("Không có dữ liệu");
        } else {
            pieChart.getData().addAll(pieChartData);
            System.out.println("Đã thêm dữ liệu vào PieChart");
        }
    }
    
    private void updateSectorLineChart() {
        // Lấy dữ liệu từ service
        Map<Integer, Long> sectorCountByYear = sectorService.getSectorCountByYear();

        // Tạo series cho biểu đồ đường
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Số lượng phân khu");

        // Thêm dữ liệu vào series
        for (Map.Entry<Integer, Long> entry : sectorCountByYear.entrySet()) {
            series.getData().add(new XYChart.Data<>(String.valueOf(entry.getKey()), entry.getValue()));
        }

        // Xóa dữ liệu cũ và thêm series mới vào biểu đồ
        sectorLineChart.getData().clear();
        sectorLineChart.getData().add(series);
    }
}