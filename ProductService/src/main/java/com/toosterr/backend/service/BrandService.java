package com.toosterr.backend.service;

import com.toosterr.backend.dto.AddBrandRequest;
import com.toosterr.backend.entity.Brand;
import com.toosterr.backend.repository.BrandRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {

    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public Brand add(AddBrandRequest brand) {
        try {
            Brand brandEntity = Brand.builder()
                    .name(brand.getName())
                    .description(brand.getDescription())
                    .build();
            return brandRepository.save(brandEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
