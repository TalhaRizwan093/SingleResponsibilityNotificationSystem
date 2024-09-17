package com.toosterr.backend.repository;

import com.toosterr.backend.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepository extends JpaRepository<Brand, Integer> {

    List<Brand> findAll();

    Optional<Brand> getBrandById(Integer brandId);
}
