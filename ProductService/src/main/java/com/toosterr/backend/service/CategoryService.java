package com.toosterr.backend.service;

import com.toosterr.backend.dto.AddCategoryRequest;
import com.toosterr.backend.entity.Category;
import com.toosterr.backend.exception.categoryException.CategoryNotFoundException;
import com.toosterr.backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category add(AddCategoryRequest category) {

        Category categoryEntity = Category.builder()
                .categoryName(category.getName())
                .description(category.getDescription())
                .build();
        return categoryRepository.save(categoryEntity);

    }

    public boolean deleteCategory(int id) {
        if(categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        } else{
            throw new CategoryNotFoundException("Category Not found");
        }
    }

    public List<Category> getCategoriesByAttributeId(Integer id) {
        return categoryRepository.findByCategoryAttributes_Id(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category Not found"));
    }

    public List<Category> getCategoriesByProductId(Integer id) {
        return categoryRepository.findByProductCategories_Id(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category Not found"));
    }
}
