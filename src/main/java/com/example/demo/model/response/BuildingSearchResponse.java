package com.example.demo.model.response;


import com.example.demo.model.dto.AbstractDTO;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildingSearchResponse extends AbstractDTO {
    private String code;
    private String name;
    private String address;
    private String developerName;
    private String rank;
    private Double totalArea;
    private Long minSellingPrice;
    private Long maxSellingPrice;
    private Integer numberEle;
    private Integer numberLivingFloor;
    private Integer numberBasement;
    private Long bikeParkingMonthly;
    private Long carParkingMonthly;
}
