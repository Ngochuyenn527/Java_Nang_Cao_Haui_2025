package com.example.demo.views;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.util.Base64Utils;

import com.example.demo.model.dto.BuildingDTO;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class BuildingEditViewController {

    @FXML private TextField txtCode, txtName, txtWard, txtDistrict, txtDeveloperName, txtRank;
    @FXML private TextField txtTotalArea, txtMinPrice, txtMaxPrice;
    @FXML private TextField txtNumberEle, txtNumberLivingFloor, txtNumberBasement;
    @FXML private TextField txtBikeParkingMonthly, txtCarParkingMonthly;
    
    @FXML private Button btnEdit;

    private Long buildingId; // Biến để lưu ID của tòa nhà

    public void setData(BuildingDTO dto, Long buildingId) {
        this.buildingId = buildingId; // Lưu ID khi truyền đến
        txtCode.setText(dto.getCode());
        txtName.setText(dto.getName());
        txtDeveloperName.setText(dto.getDeveloperName());
        txtRank.setText(dto.getRank());
        txtTotalArea.setText(String.valueOf(dto.getTotalArea()));
        txtMinPrice.setText(String.valueOf(dto.getMinSellingPrice()));
        txtMaxPrice.setText(String.valueOf(dto.getMaxSellingPrice()));
        txtNumberEle.setText(String.valueOf(dto.getNumberEle()));
        txtNumberLivingFloor.setText(String.valueOf(dto.getNumberLivingFloor()));
        txtNumberBasement.setText(String.valueOf(dto.getNumberBasement()));
        txtBikeParkingMonthly.setText(String.valueOf(dto.getBikeParkingMonthly()));
        txtCarParkingMonthly.setText(String.valueOf(dto.getCarParkingMonthly()));

        // Cắt address
        String[] parts = dto.getAddress().split(",");
        if (parts.length >= 3) {
            txtWard.setText(parts[0].trim());
            txtDistrict.setText(parts[1].trim());
        } else {
            txtWard.setText("");
            txtDistrict.setText("");
        }
    }

    // Xử lý sự kiện nhấn nút sửa
    @FXML
    public void handleUpdateBuilding() {
        BuildingDTO buildingDTO = new BuildingDTO();

        try {
            buildingDTO.setCode(txtCode.getText());
            buildingDTO.setName(txtName.getText());
            buildingDTO.setDeveloperName(txtDeveloperName.getText());
            buildingDTO.setRank(txtRank.getText());

            buildingDTO.setTotalArea(Double.parseDouble(txtTotalArea.getText()));
            buildingDTO.setMinSellingPrice(Long.parseLong(txtMinPrice.getText()));
            buildingDTO.setMaxSellingPrice(Long.parseLong(txtMaxPrice.getText()));
            buildingDTO.setNumberEle(Integer.parseInt(txtNumberEle.getText()));
            buildingDTO.setNumberLivingFloor(Integer.parseInt(txtNumberLivingFloor.getText()));
            buildingDTO.setNumberBasement(Integer.parseInt(txtNumberBasement.getText()));
            buildingDTO.setBikeParkingMonthly(Long.parseLong(txtBikeParkingMonthly.getText()));
            buildingDTO.setCarParkingMonthly(Long.parseLong(txtCarParkingMonthly.getText()));

            String fullAddress = txtWard.getText() + ", " + txtDistrict.getText() + ", TP. Hà Nội";
            buildingDTO.setAddress(fullAddress);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi định dạng");
            alert.setHeaderText("Dữ liệu nhập vào không hợp lệ");
            alert.setContentText("Vui lòng kiểm tra lại các trường số (diện tích, giá, tầng,...)");
            alert.showAndWait();
            return; // Dừng xử lý nếu dữ liệu không hợp lệ
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Lỗi không xác định");
            alert.setHeaderText("Đã xảy ra lỗi khi xử lý dữ liệu.");
            alert.setContentText("Chi tiết: " + e.getMessage());
            alert.showAndWait();
            return;
        }

        // Gửi yêu cầu PUT cập nhật tòa nhà
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8081/api/building/" + buildingId;

        String username = "admin";
        String password = "123456";
        String auth = username + ":" + password;
        String encodedAuth = Base64Utils.encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);
        HttpEntity<BuildingDTO> entity = new HttpEntity<>(buildingDTO, headers);

        try {
            ResponseEntity<BuildingDTO> response = restTemplate.exchange(url, HttpMethod.PUT, entity, BuildingDTO.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Cập nhật thành công");
                alert.setHeaderText("Tòa nhà đã được cập nhật thành công!");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Cập nhật thất bại");
                alert.setHeaderText("Có lỗi khi cập nhật tòa nhà.");
                alert.setContentText("Mã lỗi: " + response.getStatusCode());
                alert.showAndWait();
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Cập nhật thất bại");
            alert.setHeaderText("Có lỗi khi cập nhật tòa nhà.");
            alert.setContentText("Lỗi: " + e.getMessage());
            alert.showAndWait();
        }
    }

}
