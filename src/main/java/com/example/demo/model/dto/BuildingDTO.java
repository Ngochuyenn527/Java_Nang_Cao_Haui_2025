package com.example.demo.model.dto;

import lombok.*;

//hứng data được điền trong các field của chức năng thêm, sửa tòa nhà
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildingDTO  extends AbstractDTO {
    private String code;
    private String name;
    private String address;
    private String developerName;
    private String rank;
    private Double totalArea;
    private Double minSellingPrice;
    private Double maxSellingPrice;
    private Integer numberEle;
    private Integer numberLivingFloor;
    private Integer numberBasement;
    private Long bikeParkingMonthly;
    private Long carParkingMonthly;
}
