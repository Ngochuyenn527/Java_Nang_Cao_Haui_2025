package com.example.demo.controller;

import com.example.demo.model.dto.ApartmentDTO;
import com.example.demo.service.ApartmentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api/apartment")
public class ApartmentController {

    @Autowired
    private ApartmentService apartmentService;

    @Operation(summary = "API get all apartments")
    @GetMapping
    public ResponseEntity<List<ApartmentDTO>> getAllApartments() {
        List<ApartmentDTO> apartments = apartmentService.getAllApartments();
        return new ResponseEntity<>(apartments, HttpStatus.OK);
    }

    @Operation(summary = "API get apartment by id")
    @GetMapping("/{id}")
    public ResponseEntity<ApartmentDTO> getApartmentById(@PathVariable Long id) {
        ApartmentDTO apartmentDTO = apartmentService.getApartmentById(id);
        return new ResponseEntity<>(apartmentDTO, HttpStatus.OK);
    }

    @Operation(summary = "API get apartments by floor")
    @GetMapping("/floor/{floor}")
    public ResponseEntity<List<ApartmentDTO>> getApartmentsByFloor(@PathVariable Integer floor) {
        List<ApartmentDTO> apartments = apartmentService.getApartmentsByFloor(floor);
        return new ResponseEntity<>(apartments, HttpStatus.OK);
    }

    @Operation(summary = "API get apartments by status")
    @GetMapping("/status/{status}")
    public ResponseEntity<List<ApartmentDTO>> getApartmentsByStatus(@PathVariable String status) {
        List<ApartmentDTO> apartments = apartmentService.getApartmentsByStatus(status);
        return new ResponseEntity<>(apartments, HttpStatus.OK);
    }

    @Operation(summary = "API create new apartment")
    @PostMapping
    public ResponseEntity<ApartmentDTO> addApartment(@RequestBody ApartmentDTO apartmentDTO) {
        ApartmentDTO createdApartment = apartmentService.addApartment(apartmentDTO);
        return new ResponseEntity<>(createdApartment, HttpStatus.CREATED);
    }

    @Operation(summary = "API update apartment by id")
    @PutMapping("/{id}")
    public ResponseEntity<ApartmentDTO> updateApartment(@PathVariable Long id, @RequestBody ApartmentDTO apartmentDTO) {
        ApartmentDTO updatedApartment = apartmentService.updateApartment(id, apartmentDTO);
        return new ResponseEntity<>(updatedApartment, HttpStatus.OK);
    }

    @Operation(summary = "API delete apartment by id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApartment(@PathVariable Long id) {
        apartmentService.deleteApartment(id);
        return ResponseEntity.ok("Deleted successfully!");
    }
}
