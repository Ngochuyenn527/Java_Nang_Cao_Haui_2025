package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "apartment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApartmentEntity extends BaseEntity {

    @JsonProperty("code")
    @Column(name = "code", unique = true)
    private String code;

    @JsonProperty("name")
    @Column(name = "name")
    private String name;

    @JsonProperty("floor")
    @Column(name = "floor")
    private Integer floor;

    @JsonProperty("area")
    @Column(name = "area")
    private Double area;

    @JsonProperty("number_of_bedrooms")
    @Column(name = "number_of_bedrooms")
    private Integer numberOfBedrooms;

    @JsonProperty("number_of_bathrooms")
    @Column(name = "number_of_bathrooms")
    private Integer numberOfBathrooms;

    @JsonProperty("price")
    @Column(name = "price")
    private Long price;

    @JsonProperty("status")
    @Column(name = "status")
    private String status;

    @JsonProperty("facing_direction")
    @Column(name = "facing_direction")
    private String facingDirection;


    @JsonProperty("description")
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @JsonProperty("view")
    @Column(name = "view")
    private String view;

    @JsonProperty("electricity_price_per_kwh")
    @Column(name = "electricity_price_per_kwh")
    private Integer electricityPricePerKwh;

    @JsonProperty("water_price_per_m3")
    @Column(name = "water_price_per_m3")
    private Integer waterPricePerM3;

    @JsonProperty("ceiling_height")
    @Column(name = "ceiling_height")
    private Double ceilingHeight;

    // Mối quan hệ với BuildingEntity (1 apt thuộc 1 building)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    private BuildingEntity building;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Double getArea() {
        return area;
    }

    public Integer getFloor() {
        return floor;
    }

    public Integer getNumberOfBedrooms() {
        return numberOfBedrooms;
    }

    public Integer getNumberOfBathrooms() {
        return numberOfBathrooms;
    }

    public Long getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public String getFacingDirection() {
        return facingDirection;
    }

    public String getDescription() {
        return description;
    }

    public Integer getElectricityPricePerKwh() {
        return electricityPricePerKwh;
    }

    public String getView() {
        return view;
    }

    public Integer getWaterPricePerM3() {
        return waterPricePerM3;
    }

    public Double getCeilingHeight() {
        return ceilingHeight;
    }

    public BuildingEntity getBuilding() {
        return building ;
    }
}

