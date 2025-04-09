package com.example.demo.model.request;

import com.example.demo.model.dto.AbstractDTO;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BuildingSearchRequest extends AbstractDTO {
    private String name;
    private String address;
    private String rank;
    private Double areaFrom;
    private Double areaTo;
    private Double sellingPrice;
    private Integer numberLivingFloor;
    private Integer numberBasement;
}


