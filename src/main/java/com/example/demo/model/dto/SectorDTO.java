package com.example.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SectorDTO extends AbstractDTO {

    @JsonProperty("code")
    private String code;

    @JsonProperty("name")
    private String name;

    @JsonProperty("location")
    private String location;

    @JsonProperty("totalArea")  // Ánh xạ từ JSON với snake_case thành total_area
    private Double totalArea;

    @JsonProperty("maxFloors")  // Ánh xạ từ JSON với snake_case thành max_floors
    private Integer maxFloors;

    @JsonProperty("description")  // Ánh xạ từ JSON với snake_case thành description
    private String description;

    @JsonProperty("managerName")  // Ánh xạ từ JSON với snake_case thành manager_name
    private String managerName;

    @JsonProperty("status")  // Ánh xạ từ JSON với snake_case thành status
    private String status;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("startDate")  // Ánh xạ từ JSON với snake_case thành start_date
    private Date startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("expectedCompletionDate")  // Ánh xạ từ JSON với snake_case thành expected_completion_date
    private Date expectedCompletionDate;

}

