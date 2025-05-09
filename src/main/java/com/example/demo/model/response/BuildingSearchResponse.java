package com.example.demo.model.response;

import com.example.demo.model.dto.AbstractDTO;
import com.example.demo.model.dto.SectorDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//hứng data kết quả tòa nhà tìm kiếm được giống BuildingDTO
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildingSearchResponse extends AbstractDTO {

    private SectorDTO sector;

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
