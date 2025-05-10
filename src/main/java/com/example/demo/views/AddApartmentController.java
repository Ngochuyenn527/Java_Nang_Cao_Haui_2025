package com.example.demo.views;

import com.example.demo.entity.BuildingEntity;
import com.example.demo.model.dto.ApartmentDTO;
import com.example.demo.model.dto.BuildingDTO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class AddApartmentController {
    @FXML private TextField txtBuildingId, txtApartmentId, txtApartmentName, txtFloor, txtArea, txtBedrooms, txtBathrooms, txtPrice, txtDirection, txtDescription, txtView, txtElectricityFee, txtWaterFee, txtCeilingHeight;
    @FXML private ComboBox<String> cbStatus;
    @FXML private Button btnSave, btnCancel;

    // Tạo mới controller gọi xử lý
    private ApartmentViewController apartmentViewController = new ApartmentViewController();

    public void initialize() {
        // Có thể thêm khởi tạo nếu cần
    }

    @FXML
    private boolean handleAdd() {
        System.out.println("Bắt đầu thêm mới căn hộ");
        try {
            Long buildingId = Long.parseLong(txtBuildingId.getText().trim());
            String code = txtApartmentId.getText().trim();
            String name = txtApartmentName.getText().trim();
            String status = cbStatus.getValue();
            int floor = Integer.parseInt(txtFloor.getText().trim());
            double price = Double.parseDouble(txtPrice.getText().trim());
            String direction = txtDirection.getText().trim();
            double ceilingHeight = Double.parseDouble(txtCeilingHeight.getText().trim());
            double area = Double.parseDouble(txtArea.getText().trim());
            int bedrooms = Integer.parseInt(txtBedrooms.getText().trim());
            int bathrooms = Integer.parseInt(txtBathrooms.getText().trim());
            double electricityFee = Double.parseDouble(txtElectricityFee.getText().trim());
            double waterFee = Double.parseDouble(txtWaterFee.getText().trim());
            String description = txtDescription.getText().trim();
            String view = txtView.getText().trim();

            if (status == null || status.isEmpty()) {
                throw new IllegalArgumentException("Status cannot be empty");
            }

            ApartmentDTO newApartment = new ApartmentDTO();
            newApartment.setId(buildingId);
            newApartment.setCode(code);
            newApartment.setName(name);
            newApartment.setStatus(status);
            newApartment.setFloor(floor);
            newApartment.setPrice((long) price);
            newApartment.setFacingDirection(direction);
            newApartment.setCeilingHeight(ceilingHeight);
            newApartment.setArea(area);
            newApartment.setNumberOfBedrooms(bedrooms);
            newApartment.setNumberOfBathrooms(bathrooms);
            newApartment.setElectricityPricePerKwh((int) electricityFee);
            newApartment.setWaterPricePerM3((int) waterFee);
            newApartment.setDescription(description);
            newApartment.setView(view);


            // Gọi hàm thêm mới
            apartmentViewController.addApartment(newApartment);

            System.out.println("Thêm căn hộ thành công!");

            btnSave.getScene().getWindow().hide();
            return true;
        } catch (NumberFormatException e) {
            System.err.println("Lỗi định dạng số: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.err.println("Dữ liệu không hợp lệ: " + e.getMessage());
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @FXML
    private void cancel() {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }
}
