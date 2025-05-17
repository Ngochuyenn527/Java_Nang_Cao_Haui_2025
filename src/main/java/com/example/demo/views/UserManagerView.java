package com.example.demo.views;

import com.example.demo.model.dto.UserDTO;
import com.example.demo.views.CommonUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class UserManagerView {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private Long selectedUserId;

    public UserManagerView() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    public void setUpTableColumns(TableColumn<UserDTO, String> nameUser, TableColumn<UserDTO, String> fullNameUser,
                                  TableColumn<UserDTO, String> sdtUser, TableColumn<UserDTO, String> emailUser,
                                  TableColumn<UserDTO, String> roleUser) {
        nameUser.setCellValueFactory(new PropertyValueFactory<>("userName"));
        fullNameUser.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        sdtUser.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailUser.setCellValueFactory(new PropertyValueFactory<>("email"));
        roleUser.setCellValueFactory(new PropertyValueFactory<>("roleCode"));
    }

    public void fetchUserDataFromApi(TableView<UserDTO> userList) {
        try {
            String url = "http://localhost:8081/api/user";
            HttpHeaders headers = CommonUtils.createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<UserDTO> users = objectMapper.readValue(
                        response.getBody(),
                        new TypeReference<>() {}
                );
                userList.setItems(FXCollections.observableArrayList(users));
                System.out.println("Đã tải " + users.size() + " người dùng.");
            } else {
                CommonUtils.showError("Không thể lấy dữ liệu người dùng: " + response.getStatusCode());
            }
        } catch (Exception e) {
            CommonUtils.showError("Lỗi khi gọi API người dùng: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void loadUserList(TableView<UserDTO> userList) {
        fetchUserDataFromApi(userList);
    }

    public void handleAddUser(Pane paneUserManager, Pane paneEditUser) {
        paneUserManager.setVisible(false);
        paneUserManager.setManaged(false);
        paneEditUser.setVisible(true);
        paneEditUser.setManaged(true);
        CommonUtils.clearAllTextFieldsIn(paneEditUser); // Xóa dữ liệu form trước khi thêm mới
    }

    public void handleAddUser_2(Pane paneUserManager, Pane paneEditUser, TableView<UserDTO> userList,
                                TextField txtTenNguoiDung, TextField txtHoTen, TextField txtSoDienThoai,
                                TextField txtEmail, ComboBox<String> cbbStatusUser) {
        UserDTO user = getUserInputFromForm(txtTenNguoiDung, txtHoTen, txtSoDienThoai, txtEmail, cbbStatusUser);
        if (user == null) return;

        try {
            String json = objectMapper.writeValueAsString(user);
            HttpHeaders headers = CommonUtils.createAuthHeaders();
            HttpEntity<String> request = new HttpEntity<>(json, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    "http://localhost:8081/api/user",
                    HttpMethod.POST,
                    request,
                    String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                CommonUtils.showAlert(Alert.AlertType.INFORMATION, "Thành công",
                        "Đã thêm tài khoản mới!", null);
                backToUserManager(paneUserManager, paneEditUser);
                loadUserList(userList);
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

    public void handleEditUser(TableView<UserDTO> userList, Pane paneUserManager, Pane paneEditUser,
                               TextField txtTenNguoiDung, TextField txtHoTen, TextField txtSoDienThoai,
                               TextField txtEmail, ComboBox<String> cbbStatusUser) {
        UserDTO selected = userList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            CommonUtils.showAlert(Alert.AlertType.WARNING, "Chưa chọn tài khoản",
                    null, "Vui lòng chọn một tài khoản trong bảng để chỉnh sửa.");
            return;
        }

        selectedUserId = selected.getId();
        paneUserManager.setVisible(false);
        paneUserManager.setManaged(false);
        paneEditUser.setVisible(true);
        paneEditUser.setManaged(true);

        txtTenNguoiDung.setText(selected.getUserName());
        txtHoTen.setText(selected.getFullName());
        txtSoDienThoai.setText(selected.getPhone());
        txtEmail.setText(selected.getEmail());
        cbbStatusUser.setValue(selected.getRoleCode());
    }

    public void handleUpdateUser(Pane paneUserManager, Pane paneEditUser, TableView<UserDTO> userList,
                                 TextField txtTenNguoiDung, TextField txtHoTen, TextField txtSoDienThoai,
                                 TextField txtEmail, ComboBox<String> cbbStatusUser) {
        UserDTO user = getUserInputFromForm(txtTenNguoiDung, txtHoTen, txtSoDienThoai, txtEmail, cbbStatusUser);
        if (user == null) return;

        try {
            String json = objectMapper.writeValueAsString(user);
            HttpHeaders headers = CommonUtils.createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(json, headers);

            String url = "http://localhost:8081/api/user/" + selectedUserId;
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.PUT, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                CommonUtils.showAlert(Alert.AlertType.INFORMATION, "Cập nhật thành công",
                        "Tài khoản đã được cập nhật thành công!", null);
                backToUserManager(paneUserManager, paneEditUser);
                loadUserList(userList);
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

    public void handleDeleteUser(TableView<UserDTO> userList) {
        UserDTO selected = userList.getSelectionModel().getSelectedItem();
        if (selected == null) {
            CommonUtils.showAlert(Alert.AlertType.WARNING, "Chưa chọn tài khoản",
                    null, "Vui lòng chọn một tài khoản cần xóa!");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Xác nhận xóa");
        confirm.setHeaderText("Xóa tài khoản " + selected.getUserName() + "?");
        confirm.setContentText("Thao tác này không thể hoàn tác.");

        confirm.showAndWait().ifPresent(result -> {
            if (result.getText().equals("OK")) {
                try {
                    String url = "http://localhost:8081/api/user/" + selected.getId();
                    HttpHeaders headers = CommonUtils.createAuthHeaders();
                    HttpEntity<Void> entity = new HttpEntity<>(headers);
                    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.DELETE, entity, String.class);

                    if (response.getStatusCode() == HttpStatus.OK) {
                        CommonUtils.showAlert(Alert.AlertType.INFORMATION, "Xóa thành công",
                                "Đã xóa tài khoản thành công!", null);
                        loadUserList(userList);
                    } else {
                        CommonUtils.handleApiError(response.getStatusCode(), response.getBody());
                    }
                } catch (Exception e) {
                    CommonUtils.showAlert(Alert.AlertType.ERROR, "Xóa thất bại",
                            "Lỗi khi xóa tài khoản: ", e.getMessage());
                }
            }
        });
    }

    public void handleSearchByUsername(TableView<UserDTO> userList, TextField txtSearchUsername) {
        String username = txtSearchUsername.getText().trim();
        if (username.isEmpty()) {
            loadUserList(userList);
            return;
        }

        try {
            String url = "http://localhost:8081/api/user/username/" + username;
            HttpHeaders headers = CommonUtils.createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                UserDTO user = objectMapper.readValue(response.getBody(), UserDTO.class);
                userList.setItems(FXCollections.observableArrayList(user));
            } else {
                CommonUtils.showAlert(Alert.AlertType.WARNING, "Không tìm thấy",
                        "Không tìm thấy người dùng: ", String.valueOf(response.getStatusCode()));
            }
        } catch (Exception e) {
            CommonUtils.showAlert(Alert.AlertType.ERROR, "Lỗi tìm kiếm",
                    "Lỗi khi tìm kiếm người dùng: ", e.getMessage());
        }
    }

    public void handleSearchByRole(TableView<UserDTO> userList, ComboBox<String> cbbSearchRole) {
        String roleCode = cbbSearchRole.getValue();
        if (roleCode == null || roleCode.isEmpty()) {
            loadUserList(userList);
            CommonUtils.showAlert(Alert.AlertType.WARNING, "Chưa chọn vai trò",
                    null, "Vui lòng chọn một vai trò để tìm kiếm!");
            return;
        }

        try {
            String url = "http://localhost:8081/api/user/role/" + roleCode;
            HttpHeaders headers = CommonUtils.createAuthHeaders();
            HttpEntity<String> entity = new HttpEntity<>(headers);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                List<UserDTO> users = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
                userList.setItems(FXCollections.observableArrayList(users));
            } else {
                CommonUtils.showAlert(Alert.AlertType.WARNING, "Không tìm thấy",
                        "Không tìm thấy người dùng với vai trò này: ", String.valueOf(response.getStatusCode()));
            }
        } catch (Exception e) {
            CommonUtils.showAlert(Alert.AlertType.ERROR, "Lỗi tìm kiếm",
                    "Lỗi khi tìm kiếm theo vai trò: ", e.getMessage());
        }
    }

    private UserDTO getUserInputFromForm(TextField txtTenNguoiDung, TextField txtHoTen, TextField txtSoDienThoai,
                                        TextField txtEmail, ComboBox<String> cbbStatusUser) {
        try {
            // Kiểm tra các trường bắt buộc
            if (txtTenNguoiDung.getText().isEmpty() || txtHoTen.getText().isEmpty() ||
                txtEmail.getText().isEmpty() || cbbStatusUser.getValue() == null) {
                CommonUtils.showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu",
                        "Dữ liệu không hợp lệ", "Vui lòng nhập đầy đủ các trường bắt buộc.");
                return null;
            }

            UserDTO dto = new UserDTO();
            dto.setUserName(txtTenNguoiDung.getText());
            dto.setFullName(txtHoTen.getText());
            dto.setPhone(txtSoDienThoai.getText());
            dto.setEmail(txtEmail.getText());
            dto.setRoleCode(cbbStatusUser.getValue());
            return dto;
        } catch (Exception e) {
            CommonUtils.showAlert(Alert.AlertType.ERROR, "Lỗi nhập liệu",
                    "Dữ liệu không hợp lệ", "Vui lòng kiểm tra lại các trường nhập liệu.");
            return null;
        }
    }

    public void handleUserMng(Pane paneUserManager, Pane paneApartManager, Pane paneSectorManager,
                              Pane paneBuildingManager, TableView<UserDTO> userList, Pane... editPanes) {
        CommonUtils.showOnly(paneUserManager, paneApartManager, paneSectorManager, paneBuildingManager, paneUserManager);
        loadUserList(userList);
        CommonUtils.hideEditPanes(editPanes);
    }

    public void backToUserManager(Pane paneUserManager, Pane paneEditUser) {
        paneUserManager.setVisible(true);
        paneUserManager.setManaged(true);
        paneEditUser.setVisible(false);
        paneEditUser.setManaged(false);
        CommonUtils.clearAllTextFieldsIn(paneEditUser);
    }
}