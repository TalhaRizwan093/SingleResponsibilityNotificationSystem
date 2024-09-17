package com.toosterr.backend.service;

import com.toosterr.backend.dto.AddAttributeRequest;
import com.toosterr.backend.entity.Attribute;
import com.toosterr.backend.exception.categoryException.CategoryNotFoundException;
import com.toosterr.backend.repository.AttributeRepository;
import com.toosterr.backend.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttributeService {

    private final AttributeRepository attributeRepository;
    private final CategoryRepository categoryRepository;

    public AttributeService(AttributeRepository attributeRepository, CategoryRepository categoryRepository) {
        this.attributeRepository = attributeRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<Attribute> findAll() {
        return attributeRepository.findAll();
    }

    public Attribute add(AddAttributeRequest attribute) {
            Attribute attributeEntity = Attribute.builder()
                    .name(attribute.getName())
                    .type(attribute.getType())
                    .category(categoryRepository.getCategoryById(attribute.getCategoryId())
                            .orElseThrow(() -> new CategoryNotFoundException("Given Category Not Found")))
                    .build();
            return  attributeRepository.save(attributeEntity);
    }
}
