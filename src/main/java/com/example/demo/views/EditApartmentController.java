package com.example.demo.views;

import com.example.demo.entity.ApartmentEntity;
import com.example.demo.entity.BuildingEntity;
import com.example.demo.model.dto.ApartmentDTO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.springframework.stereotype.Component;

@Component
public class EditApartmentController {
    @FXML private TextField txtBuildingId, txtApartmentId, txtApartmentName, txtFloor, txtArea, txtBedrooms, txtBathrooms, txtPrice, txtDirection, txtDescription, txtView, txtElectricityFee, txtWaterFee, txtCeilingHeight;
    @FXML private ComboBox<String> cbStatus;
    @FXML private Button btnSave, btnCancel;

//    @Autowired
//    private ApartmentConverter apartmentConverter;
//
//    @Autowired
    private ApartmentViewController apartmentViewController = new ApartmentViewController() ;

    public void initialize() {
//        if (apartmentConverter == null) {
//            System.err.println("Error: ApartmentConverter is null in EditApartmentController");
//        } else {
//            System.out.println("EditApartmentController: ApartmentConverter injected successfully: " + apartmentConverter);
//        }
//        if (apartmentService == null) {
//            System.err.println("Error: ApartmentService is null in EditApartmentController");
//        } else {
//            System.out.println("EditApartmentController: ApartmentService injected successfully: " + apartmentService);
//        }
    }

    public void setApartmentData(ApartmentEntity apartment) {
        if (apartment == null ) {
            throw new IllegalArgumentException("Apartment or its building is null");
        }
        txtBuildingId.setText(String.valueOf(apartment.getBuilding().getId()));
        txtApartmentId.setText(apartment.getCode());
        txtApartmentName.setText(apartment.getName());
        txtFloor.setText(String.valueOf(apartment.getFloor()));
        txtArea.setText(String.valueOf(apartment.getArea()));
        txtBedrooms.setText(String.valueOf(apartment.getNumberOfBedrooms()));
        txtBathrooms.setText(String.valueOf(apartment.getNumberOfBathrooms()));
        txtPrice.setText(String.valueOf(apartment.getPrice()));
        cbStatus.setValue(apartment.getStatus());
//        txtDirection.setText(apartment.getFacingDirection() != null ? apartment.getFacingDirection() : "");
        txtDirection.setText(apartment.getFacingDirection());
//        txtDescription.setText(apartment.getDescription() != null ? apartment.getDescription() : "");
        txtDescription.setText(apartment.getDescription());
//        txtView.setText(apartment.getView() != null ? apartment.getView() : "");
        txtView.setText(apartment.getView());
        txtElectricityFee.setText(String.valueOf(apartment.getElectricityPricePerKwh()));
        txtWaterFee.setText(String.valueOf(apartment.getWaterPricePerM3()));
        txtCeilingHeight.setText(String.valueOf(apartment.getCeilingHeight()));
    }

    @FXML
    private boolean handleSave() {
        System.out.println("Bat dau handleSave");
        try {
            Long id = Long.parseLong(txtBuildingId.getText());
            String code = txtApartmentId.getText().trim();
            String name = txtApartmentName.getText().trim();
            String status = cbStatus.getValue();
            int floor = Integer.parseInt(txtFloor.getText().trim());
            String buildingId = txtBuildingId.getText().trim();
            double price = Double.parseDouble(txtPrice.getText().trim());
            String mainDirection = txtDirection.getText().trim();
            double ceilingHeight = Double.parseDouble(txtCeilingHeight.getText().trim());
            double area = Double.parseDouble(txtArea.getText().trim());
            int numberOfBedrooms = Integer.parseInt(txtBedrooms.getText().trim());
            int numberOfBathrooms = Integer.parseInt(txtBathrooms.getText().trim());
            double electricityCost = Double.parseDouble(txtElectricityFee.getText().trim());
            double waterCost = Double.parseDouble(txtWaterFee.getText().trim());
            String description = txtDescription.getText().trim();

            if (status == null || status.isEmpty()) {
                throw new IllegalArgumentException("Status cannot be empty");
            }

            ApartmentDTO updated = new ApartmentDTO();

            updated.setId(id);
            updated.setCode(code);
            updated.setName(name);
            updated.setStatus(status);
            updated.setFloor(floor);
            BuildingEntity buildingEntity = new BuildingEntity();
            buildingEntity.setId(Long.parseLong(buildingId));
//            updated.setBuilding(buildingEntity);
            updated.setPrice(Double.valueOf(price).longValue());
            updated.setFacingDirection(mainDirection);
            updated.setCeilingHeight(ceilingHeight);
            updated.setArea(area);
            updated.setNumberOfBedrooms(numberOfBedrooms);
            updated.setNumberOfBathrooms(numberOfBathrooms);
            updated.setElectricityPricePerKwh((int) electricityCost);
            updated.setWaterPricePerM3((int) waterCost);
            updated.setDescription(description);
            System.out.println("ID cua can toa nha :"+id);

//            ApartmentDTO result = apartmentConverter.convertToApartmentDto(updated);
            apartmentViewController.updateApartment(id, updated);

            System.out.println("Cập nhật thành công!");

            btnSave.getScene().getWindow().hide();
            return true;
        } catch (NumberFormatException e) {
            System.err.println("Invalid numeric input: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid input: " + e.getMessage());
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