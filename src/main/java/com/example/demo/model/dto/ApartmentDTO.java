package com.example.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApartmentDTO extends AbstractDTO {

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("floor")
    private Integer floor;

    @JsonProperty("area")
    private Double area;

    @JsonProperty("number_of_bedrooms")
    private Integer numberOfBedrooms;

    @JsonProperty("number_of_bathrooms")
    private Integer numberOfBathrooms;

    @JsonProperty("price")
    private Long price;

    @JsonProperty("status")
    private String status;

    @JsonProperty("facing_direction")
    private String facingDirection;


    @JsonProperty("description")
    private String description;

    @JsonProperty("view")
    private String view;

    @JsonProperty("electricity_price_per_kwh")
    private Integer electricityPricePerKwh;

    @JsonProperty("water_price_per_m3")
    private Integer waterPricePerM3;

    @JsonProperty("ceiling_height")
    private Double ceilingHeight;

    private BuildingDTO building;

}

