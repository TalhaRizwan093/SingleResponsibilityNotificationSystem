package com.toosterr.backend.controller;

import com.toosterr.backend.dto.AddCategoryRequest;
import com.toosterr.backend.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        return new ResponseEntity<>(categoryService.findAll(), HttpStatus.OK);
    }

    @PostMapping("/category/add")
    public ResponseEntity<?> addCategory(@RequestBody @Valid AddCategoryRequest category) {
        return new ResponseEntity<>(categoryService.add(category), HttpStatus.OK);
    }

    @DeleteMapping("/category/delete/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable int id){
        return new ResponseEntity<>(categoryService.deleteCategory(id), HttpStatus.OK);
    }

    @GetMapping("/category/attribute/{id}")
    public ResponseEntity<?> getCategoriesByAttribute(@PathVariable Integer id) {
        return new ResponseEntity<>(categoryService.getCategoriesByAttributeId(id), HttpStatus.OK);
    }

    @GetMapping("/category/product/{id}")
    public ResponseEntity<?> getCategoriesByProduct(@PathVariable Integer id) {
        return new ResponseEntity<>(categoryService.getCategoriesByProductId(id), HttpStatus.OK);
    }

}
