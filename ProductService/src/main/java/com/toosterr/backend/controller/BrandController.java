package com.toosterr.backend.controller;

import com.toosterr.backend.dto.AddBrandRequest;
import com.toosterr.backend.repository.BrandRepository;
import com.toosterr.backend.service.BrandService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class BrandController {

    private final BrandService brandService;

    public BrandController(BrandRepository brandRepository, BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("/brands")
    public ResponseEntity<?> getAllBrands() {
        return new ResponseEntity<>(brandService.getAllBrands(), HttpStatus.OK);
    }

    @PostMapping("/brand/add")
    public ResponseEntity<?> addBrand(@RequestBody AddBrandRequest brand) {
        return new ResponseEntity<>(brandService.add(brand), HttpStatus.OK);
    }

}
