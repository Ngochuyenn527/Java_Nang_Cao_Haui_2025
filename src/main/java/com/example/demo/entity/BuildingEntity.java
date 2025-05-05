package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "building")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BuildingEntity extends BaseEntity  {

    @Column(name = "code")
    @JsonProperty
    private String code;

    @Column(name = "name")
    @JsonProperty
    private String name;

    @Column(name = "address")
    @JsonProperty
    private String address;

    @Column(name = "developer_name")
    @JsonProperty
    private String developerName;

    //MySQL coi từ rank là từ khóa đặc biệt (reserved keyword), nên khi Hibernate tạo bảng có cột rank, nó bị lỗi cú pháp SQL.
    @Column(name = "`rank`")
    @JsonProperty
    private String rank;

    @Column(name = "total_area")
    @JsonProperty
    private Double totalArea;

    @Column(name = "min_selling_price")
    @JsonProperty
    private Long minSellingPrice;

    @Column(name = "max_selling_price")
    @JsonProperty
    private Long maxSellingPrice;

    @Column(name = "number_ele")
    @JsonProperty
    private Integer numberEle;

    @Column(name = "number_living_floor")
    @JsonProperty
    private Integer numberLivingFloor;

    @Column(name = "number_basement")
    @JsonProperty
    private Integer numberBasement;

    @Column(name = "bike_parking_monthly")
    @JsonProperty
    private Long bikeParkingMonthly;

    @Column(name = "car_parking_monthly")
    @JsonProperty
    private Long carParkingMonthly;
}
