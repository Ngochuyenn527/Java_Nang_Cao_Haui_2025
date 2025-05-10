package com.example.demo.views;

import com.example.demo.model.dto.BuildingDTO;
import com.example.demo.model.dto.SectorDTO;
import com.example.demo.model.dto.UserDTO;
import com.example.demo.views.CommonUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class ManagerViewController {

    @FXML private TextField txtname, txtward, txtdistrict, txtrank, txtareaFrom, txtareaTo, txtsellingPrice, txtnumberLivingFloor, txtnumberBasement;
    @FXML private TextField txtMaPhanKhu, txtMaToaNha, txtTenToaNha, txtDiaChiToaNha, txtTenChuDauTuToaNha, txtGiaBanThapNhat, txtGiaBanCaoNhat, txtSoThangMay, txtSoTangO, txtSoTangHam, txtPhiOTo, txtPhiXeMay, txtTongDienTichToaNha, txtHangToaNha;
    @FXML private TextField txtTenPhanKhu, txtViTri, txtTongDienTich, txtMoTa, txtTenQuanLy, txtSoTangCaoNhat, txtNgayKhoiCong, txtNgayHoanThanh;
    @FXML private TextField txtTenNguoiDung, txtHoTen, txtSoDienThoai, txtEmail, txtSearchUsername, txtSearchRole;
    @FXML private Button btnSearch, btnAdd, btnEdit, btnDelete, btnfet, btnAccMng, btnSuaPK, btnThemPK, btnHuyPK, btnSuaBuilding, btnThemBuilding, btnHuyBuilding, btnHuyCH, btnThemCH, btnSuaCH, btnHuyAcc, btnThemAcc, btnSuaAcc;
    @FXML private BorderPane paneApartManager, paneSectorManager, paneBuildingManager, paneUserManager;
    @FXML private TableView<BuildingDTO> buildingList;
    @FXML private TableColumn<BuildingDTO, Long> sectorId;
    @FXML private TableColumn<BuildingDTO, String> code, name, address, developerName, rank;
    @FXML private TableColumn<BuildingDTO, Double> totalArea;
    @FXML private TableColumn<BuildingDTO, Long> minSellingPrice, maxSellingPrice, bikeParkingMonthly, carParkingMonthly;
    @FXML private TableColumn<BuildingDTO, Integer> numberEle, numberLivingFloor, numberBasement;
    @FXML private GridPane PaneEditSector, PaneEditBuilding, PaneChangePwd, PaneEditApart, PaneEditUser;
    @FXML private TableView<SectorDTO> SectorList;
    @FXML private TableColumn<SectorDTO, String> MaPK, TenPK, ViTriPK, MoTaPK, QuanLyPK, TrangThaiPK, StartDatePK, EndDatePk;
    @FXML private TableColumn<SectorDTO, Double> TongDTPK;
    @FXML private TableColumn<SectorDTO, Integer> SoTangPK;
    @FXML private ComboBox<String> comboTrangThai, cbbSearchStatusPK, cbbStatusUser;
    @FXML private TableView<UserDTO> userList;
    @FXML private TableColumn<UserDTO, String> nameUser, fullNameUser, SdtUser, EmailUser, RoleUser;

    private final BuildingManagerView buildingManager = new BuildingManagerView();
    private final SectorManagerView sectorManager = new SectorManagerView();
    private final UserManagerView userManager = new UserManagerView();

    public void initialize() {
        buildingManager.setUpTableColumns(buildingList, sectorId, code, name, address, developerName, rank, totalArea,
                minSellingPrice, maxSellingPrice, numberEle, numberLivingFloor, numberBasement, bikeParkingMonthly, carParkingMonthly);
        sectorManager.setUpSectorTableColumns(MaPK, TenPK, ViTriPK, TongDTPK, SoTangPK, MoTaPK, QuanLyPK, TrangThaiPK, StartDatePK, EndDatePk);
        userManager.setUpTableColumns(nameUser, fullNameUser, SdtUser, EmailUser, RoleUser);
        setUpComboBoxes();
    }

    private void setUpComboBoxes() {
        ObservableList<String> statuses = FXCollections.observableArrayList(
                "Đang triển khai", "Chờ duyệt", "Hoàn thành", "Đang thi công", "Đã phê duyệt"
        );
        ObservableList<String> roles = FXCollections.observableArrayList(
                "STAFF", "MANAGER"
        );
        comboTrangThai.setItems(statuses);
        cbbSearchStatusPK.setItems(statuses);
        cbbStatusUser.setItems(roles);
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
        userManager.handleSearchByRole(userList, txtSearchRole);
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
    }

    @FXML
    public void handleSectorMng() {
        sectorManager.handleSectorMng(paneSectorManager, paneApartManager, paneBuildingManager, paneUserManager, SectorList,
                PaneEditSector, PaneEditBuilding, PaneChangePwd, PaneEditApart, PaneEditUser);
    }

    @FXML
    public void handleBuildingMng() {
        buildingManager.handleBuildingMng(paneBuildingManager, paneApartManager, paneSectorManager, paneUserManager, buildingList,
                PaneEditSector, PaneEditBuilding, PaneChangePwd, PaneEditApart, PaneEditUser);
    }

    @FXML
    public void handleApartMng() {
        CommonUtils.showOnly(paneApartManager, paneApartManager, paneSectorManager, paneBuildingManager, paneUserManager);
        CommonUtils.hideEditPanes(PaneEditSector, PaneEditBuilding, PaneChangePwd, PaneEditApart, PaneEditUser);
    }

    @FXML
    public void handleChangePassword() {
        CommonUtils.showOnly(null, paneApartManager, paneSectorManager, paneBuildingManager, paneUserManager);
        PaneChangePwd.setVisible(true);
        PaneChangePwd.setManaged(true);
    }

    @FXML
    public void handleBackSector() {
        sectorManager.backToSectorManager(paneSectorManager, PaneEditSector);
    }

    @FXML
    public void handleBackBuilding() {
        buildingManager.handleBackBuilding(paneBuildingManager, PaneEditBuilding);
    }

    @FXML
    public void handleBackBuilding_2() {
        buildingManager.handleBackBuilding_2(paneBuildingManager, PaneChangePwd);
    }

    @FXML
    public void handleBackAcc() {
        userManager.backToUserManager(paneUserManager, PaneEditUser);
    }

    @FXML
    public void handleBackApart() {
        CommonUtils.showOnly(paneApartManager, paneApartManager, paneSectorManager, paneBuildingManager, paneUserManager);
        PaneEditApart.setVisible(false);
        PaneEditApart.setManaged(false);
        CommonUtils.clearAllTextFieldsIn(PaneEditApart);
    }

    @FXML
    public void handleAddSector() {
        sectorManager.handleAddSector(paneSectorManager, PaneEditSector);
        btnThemPK.setVisible(true);
        btnThemPK.setManaged(true);
        btnSuaPK.setVisible(false);
        btnSuaPK.setManaged(false);
    }

    @FXML
    public void handleAddBuilding() {
        buildingManager.handleAddBuilding(paneBuildingManager, PaneEditBuilding);
        btnThemBuilding.setVisible(true);
        btnThemBuilding.setManaged(true);
        btnSuaBuilding.setVisible(false);
        btnSuaBuilding.setManaged(false);
    }

    @FXML
    public void handleEdit() {
        buildingManager.handleEdit(buildingList, paneBuildingManager, PaneEditBuilding, txtMaPhanKhu, txtMaToaNha,
                txtTenToaNha, txtDiaChiToaNha, txtTenChuDauTuToaNha, txtGiaBanThapNhat, txtGiaBanCaoNhat,
                txtSoThangMay, txtSoTangO, txtSoTangHam, txtHangToaNha, txtTongDienTichToaNha, txtPhiXeMay, txtPhiOTo);
        btnSuaBuilding.setVisible(true);
        btnSuaBuilding.setManaged(true);
        btnThemBuilding.setVisible(false);
        btnThemBuilding.setManaged(false);
    }

    @FXML
    public void handleUpdateBuilding() {
        buildingManager.handleUpdateBuilding(paneBuildingManager, PaneEditBuilding, buildingList, txtMaPhanKhu, txtMaToaNha,
                txtTenToaNha, txtDiaChiToaNha, txtTenChuDauTuToaNha, txtGiaBanThapNhat, txtGiaBanCaoNhat,
                txtSoThangMay, txtSoTangO, txtSoTangHam, txtHangToaNha, txtTongDienTichToaNha, txtPhiXeMay, txtPhiOTo);
    }

    @FXML
    public void handleAddBuilding_2() {
        buildingManager.handleAddBuilding_2(paneBuildingManager, PaneEditBuilding, buildingList, txtMaPhanKhu, txtMaToaNha,
                txtTenToaNha, txtDiaChiToaNha, txtTenChuDauTuToaNha, txtGiaBanThapNhat, txtGiaBanCaoNhat,
                txtSoThangMay, txtSoTangO, txtSoTangHam, txtHangToaNha, txtTongDienTichToaNha, txtPhiXeMay, txtPhiOTo);
    }

    @FXML
    public void handleAddApart() {
        CommonUtils.showOnly(null, paneApartManager, paneSectorManager, paneBuildingManager, paneUserManager);
        PaneEditApart.setVisible(true);
        PaneEditApart.setManaged(true);
        btnThemCH.setVisible(true);
        btnThemCH.setManaged(true);
        btnSuaCH.setVisible(false);
        btnSuaCH.setManaged(false);
    }

    @FXML
    public void handleEditApart() {
        CommonUtils.showOnly(null, paneApartManager, paneSectorManager, paneBuildingManager, paneUserManager);
        PaneEditApart.setVisible(true);
        PaneEditApart.setManaged(true);
        btnSuaCH.setVisible(true);
        btnSuaCH.setManaged(true);
        btnThemCH.setVisible(false);
        btnThemCH.setManaged(false);
    }

    @FXML
    public void handleThemUser() {
        userManager.handleAddUser(paneUserManager, PaneEditUser);
        btnThemAcc.setVisible(true);
        btnThemAcc.setManaged(true);
        btnSuaAcc.setVisible(false);
        btnSuaAcc.setManaged(false);
    }

    @FXML
    public void handleSuaUser() {
        userManager.handleEditUser(userList, paneUserManager, PaneEditUser, txtTenNguoiDung, txtHoTen, txtSoDienThoai,
                txtEmail, cbbStatusUser);
        btnSuaAcc.setVisible(true);
        btnSuaAcc.setManaged(true);
        btnThemAcc.setVisible(false);
        btnThemAcc.setManaged(false);
    }

    @FXML
    public void handleAddUser_2() {
        userManager.handleAddUser_2(paneUserManager, PaneEditUser, userList, txtTenNguoiDung, txtHoTen, txtSoDienThoai,
                txtEmail, cbbStatusUser);
    }

    @FXML
    public void handleUpdateUser() {
        userManager.handleUpdateUser(paneUserManager, PaneEditUser, userList, txtTenNguoiDung, txtHoTen, txtSoDienThoai,
                txtEmail, cbbStatusUser);
    }

    @FXML
    public void handleAddSector_2() {
        sectorManager.handleAddSector_2(paneSectorManager, PaneEditSector, SectorList, txtMaPhanKhu, txtTenPhanKhu,
                txtViTri, txtTongDienTich, txtMoTa, txtTenQuanLy, txtSoTangCaoNhat, txtNgayKhoiCong, txtNgayHoanThanh,
                comboTrangThai);
    }

    @FXML
    public void handleUpdateSector() {
        sectorManager.handleUpdateSector(paneSectorManager, PaneEditSector, SectorList, txtMaPhanKhu, txtTenPhanKhu,
                txtViTri, txtTongDienTich, txtMoTa, txtTenQuanLy, txtSoTangCaoNhat, txtNgayKhoiCong, txtNgayHoanThanh,
                comboTrangThai);
    }

    @FXML
    public void handleEditSector() {
        sectorManager.handleEditSector(SectorList, paneSectorManager, PaneEditSector, txtMaPhanKhu, txtTenPhanKhu,
                txtViTri, txtTongDienTich, txtMoTa, txtTenQuanLy, txtSoTangCaoNhat, txtNgayKhoiCong, txtNgayHoanThanh,
                comboTrangThai);
    }

    @FXML
    public void handleDeleteSector() {
        sectorManager.handleDeleteSector(SectorList);
    }
}