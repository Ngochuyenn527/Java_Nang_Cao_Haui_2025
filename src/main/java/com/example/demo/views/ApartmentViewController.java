package com.example.demo.views;

import com.example.demo.entity.ApartmentEntity;
import com.example.demo.model.dto.ApartmentDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.List;

public class ApartmentViewController {
    private final String BASE_URL = "http://localhost:8081/api/apartment";
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Tạo header có xác thực Basic Auth
    private HttpHeaders createAuthHeaders() {
        String auth = "nguyenvana:123456";
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodedAuth);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    // Gọi API lấy danh sách apartment
    public ObservableList<ApartmentEntity> fetchAllApartments() throws Exception {
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers); // <-- fix here

        ResponseEntity<String> response = restTemplate.exchange(
                BASE_URL,
                HttpMethod.GET,
                entity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            List<ApartmentEntity> apartmentList = objectMapper.readValue(
                    response.getBody(),
                    new TypeReference<List<ApartmentEntity>>() {}
            );
            return FXCollections.observableArrayList(apartmentList);
        } else {
            throw new RuntimeException("Failed to fetch apartments: " + response.getStatusCode());
        }
    }

    // Gọi API tìm kiếm apartment theo query
    public ObservableList<ApartmentEntity> searchApartments(String queryParam) throws Exception {
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<String> entity = new HttpEntity<>(null, headers); // <-- fix here
        String searchUrl = BASE_URL + "?" + queryParam;

        ResponseEntity<String> response = restTemplate.exchange(
                searchUrl,
                HttpMethod.GET,
                entity,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            List<ApartmentEntity> searchResult = objectMapper.readValue(
                    response.getBody(),
                    new TypeReference<List<ApartmentEntity>>() {}
            );
            return FXCollections.observableArrayList(searchResult);
        } else {
            throw new RuntimeException("Search failed: " + response.getStatusCode());
        }
    }

    public ApartmentDTO updateApartment(Long id, ApartmentDTO apartment) throws Exception {
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<ApartmentDTO> entity = new HttpEntity<>(apartment, headers);

        ResponseEntity<ApartmentDTO> response = restTemplate.exchange(
                BASE_URL + "/" + id,
                HttpMethod.PUT,
                entity,
                ApartmentDTO.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Update failed: " + response.getStatusCode());
        }
    }
    public ApartmentDTO addApartment(ApartmentDTO apartment) throws Exception {
        HttpHeaders headers = createAuthHeaders();
        HttpEntity<ApartmentDTO> entity = new HttpEntity<>(apartment, headers);

        ResponseEntity<ApartmentDTO> response = restTemplate.exchange(
                BASE_URL,
                HttpMethod.POST,
                entity,
                ApartmentDTO.class
        );

        if (response.getStatusCode() == HttpStatus.CREATED || response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Add apartment failed: " + response.getStatusCode());
        }
    }
}
