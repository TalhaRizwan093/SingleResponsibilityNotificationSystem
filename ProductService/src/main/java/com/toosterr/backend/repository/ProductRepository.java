package com.toosterr.backend.repository;

import com.toosterr.backend.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository  extends PagingAndSortingRepository<Product, Integer>, JpaRepository<Product, Integer> {

    List<Product> findAll();

    @Query("SELECT prod FROM Product prod")
    Page<Product> GetAll(Pageable pageable);

    boolean existsBySku(String sku);

    Optional<List<Product>> findByBrand_Id(Integer id);

    Optional<List<Product>> findByCategories_Id(Integer id);

    Optional<List<Product>> findByAttributes_Id(Integer id);

    Optional<Product> findOneBySku(String sku);
}
