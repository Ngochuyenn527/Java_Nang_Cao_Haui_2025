package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "building")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)

public class BuildingEntity extends BaseEntity  {

    @Column(name = "code")
    private String code;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "developer_name")
    private String developerName;

    //MySQL coi từ rank là từ khóa đặc biệt (reserved keyword), nên khi Hibernate tạo bảng có cột rank, nó bị lỗi cú pháp SQL.
    @Column(name = "`rank`")
    private String rank;

    @Column(name = "total_area")
    private Double totalArea;

    @Column(name = "min_selling_price")
    private Long minSellingPrice;

    @Column(name = "max_selling_price")
    private Long maxSellingPrice;

    @Column(name = "number_ele")
    private Integer numberEle;

    @Column(name = "number_living_floor")
    private Integer numberLivingFloor;

    @Column(name = "number_basement")
    private Integer numberBasement;

    @Column(name = "bike_parking_monthly")
    private Long bikeParkingMonthly;

    @Column(name = "car_parking_monthly")
    private Long carParkingMonthly;

    // Mối quan hệ với SectorEntity (1 building thuộc 1 sector)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sector_id")
    private SectorEntity sector;

    // Mối quan hệ với Apartment (1 building có nhiều apt)
    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ApartmentEntity> apartments;

}


