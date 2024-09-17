package com.toosterr.backend.repository;

import com.toosterr.backend.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

        List<Category> findAll();

        Optional<Category> getCategoryById(Integer categoryId);

        Optional<List<Category>> findAllByIdIn(Collection<Integer> id);

        Optional<List<Category>> findByCategoryAttributes_Id(Integer id);

        Optional<List<Category>> findByProductCategories_Id(Integer id);
}
