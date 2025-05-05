package com.example.demo.controller;

import com.example.demo.service.BuildingService;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DashboardController {

    @Autowired
    private BuildingService buildingService;

    @FXML
    private Label totalProjectsLabel, totalDistrictsLabel, avgPriceLabel, avgPricePerM2Label;
    
    @FXML
    private PieChart pieChart;
    
    @FXML
    private BarChart<String, Number> barChart;
    
    @FXML
    private BarChart<String, Number> parkingBarChart;

    @FXML
    public void initialize() {
        // Tổng số dự án
        totalProjectsLabel.setText(String.valueOf(buildingService.getDistinctProjectCount()));
        
        // Tổng số quận
        totalDistrictsLabel.setText(String.valueOf(buildingService.getDistinctAddress()));
        
        // Giá trung bình
        avgPriceLabel.setText(String.valueOf(buildingService.getDistinctAverage()));
        
        // Giá trung bình / m2
        avgPricePerM2Label.setText(String.valueOf(buildingService.getavgPricePerM2()));
        
        // Biểu đồ số lượng dự án trong quận, xã
        loadPieChartData();
        
        loadParkingBarChartData();
    }
    
    private void loadPieChartData() {
        pieChart.getData().clear();
        var pieChartData = buildingService.getPieChartDataForProjectsByDistrict();
        if (pieChartData.isEmpty()) {
            System.out.println("Không có dữ liệu để hiển thị trên PieChart");
            pieChart.setTitle("Không có dữ liệu");
        } else {
            pieChart.getData().addAll(pieChartData);
            System.out.println("Đã thêm dữ liệu vào PieChart");
        }
    }
    
    private void loadParkingBarChartData() {
        parkingBarChart.getData().clear();
        var barChartData = buildingService.getBarChartDataForProjectsByParkingFees();
        if (barChartData.stream().allMatch(series -> series.getData().isEmpty())) {
            parkingBarChart.setTitle("Không có dữ liệu");
        } else {
            parkingBarChart.getData().addAll(barChartData);
        }
    }
}