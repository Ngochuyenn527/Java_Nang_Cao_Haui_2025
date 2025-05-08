package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sector")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SectorEntity extends BaseEntity {

    @JsonProperty("code")
    @Column(name = "code", unique = true)
    private String code;

    @JsonProperty("name")
    @Column(name = "name")
    private String name;

    @JsonProperty("location")
    @Column(name = "location")
    private String location;

    @JsonProperty("totalArea")  // Ánh xạ từ JSON với snake_case thành total_area
    @Column(name = "total_area")
    private Double totalArea;

    @JsonProperty("maxFloors")  // Ánh xạ từ JSON với snake_case thành max_floors
    @Column(name = "max_floors")
    private Integer maxFloors;

    @JsonProperty("description")  // Ánh xạ từ JSON với snake_case thành description
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @JsonProperty("managerName")  // Ánh xạ từ JSON với snake_case thành manager_name
    @Column(name = "manager_name")
    private String managerName;

    @JsonProperty("status")  // Ánh xạ từ JSON với snake_case thành status
    @Column(name = "status")
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("startDate")  // Ánh xạ từ JSON với snake_case thành start_date
    @Column(name = "start_date")
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("expectedCompletionDate")  // Ánh xạ từ JSON với snake_case thành expected_completion_date
    @Column(name = "expected_completion_date")
    private Date expectedCompletionDate;


    // Mối quan hệ với BuildingEntity (1 sector có nhiều building)
//    @OneToMany(mappedBy = "sector", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<BuildingEntity> buildings;

}
