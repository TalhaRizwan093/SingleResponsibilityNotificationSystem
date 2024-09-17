package com.toosterr.backend.service;

import com.toosterr.backend.dto.SaveProductRequest;
import com.toosterr.backend.entity.*;
import com.toosterr.backend.exception.attributeExcpetion.ProductAttributeNotFoundException;
import com.toosterr.backend.exception.brandException.BrandNotFoundException;
import com.toosterr.backend.exception.categoryException.CategoryNotFoundException;
import com.toosterr.backend.exception.productException.ProductNotFoundException;
import com.toosterr.backend.repository.*;
import com.toosterr.backend.util.Helper;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final ProductAttributeRepository productAttributeRepository;
    private final Helper helper;
    private final AttributeRepository attributeRepository;

    public ProductService(ProductRepository productRepository, BrandRepository brandRepository, CategoryRepository categoryRepository, ProductCategoryRepository productCategoryRepository, ProductAttributeRepository productAttributeRepository, Helper helper, AttributeRepository attributeRepository) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.categoryRepository = categoryRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.productAttributeRepository = productAttributeRepository;
        this.helper = helper;
        this.attributeRepository = attributeRepository;
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Page<Product> findAll(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber,pageSize);

        return productRepository.GetAll(pageable);
    }

    @Transactional
    public boolean addProduct(SaveProductRequest productRequest) {
        Brand brand = brandRepository.getBrandById(productRequest.getBrandId())
                .orElseThrow(() -> new BrandNotFoundException(String.format("Brand with id {0} not found",productRequest.getBrandId())));

        Optional<List<Category>> categories = categoryRepository.findAllByIdIn(productRequest
                .getCategoryList());

        if (categories.isEmpty()) {
            throw new CategoryNotFoundException("Categories not found for the provided IDs.");
        }

        Optional<List<Attribute>> attributes = attributeRepository.findAllByIdIn(productRequest
                .getAttributeList());

        if (attributes.isEmpty()) {
            throw new ProductAttributeNotFoundException("Attributes not found for the provided IDs.");
        }

        Product prod = Product.builder()
                .name(productRequest.getName())
                .sku(generateSKU(productRequest.getName(), brand.getName(), categories.get().get(0).getCategoryName()))
                .detail(productRequest.getDetail())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .quantity(productRequest.getQuantity())
                .brand(brand)
                .build();

        Optional<List<ProductCategory>> productCategories = categories.map(categoriesData -> categoriesData
                .stream()
                .map(category -> ProductCategory.builder()
                        .category(category)
                        .product(prod)
                        .build()
                )
                .collect(Collectors.toList())
        );

        Optional<List<ProductAttribute>> productAttributes = attributes.map(attributeData -> attributeData
                .stream()
                .map(attribute -> ProductAttribute.builder()
                        .attribute(attribute)
                        .product(prod)
                        .build()
                )
                .collect(Collectors.toList())
        );

        productRepository.save(prod);
        productAttributeRepository.saveAll(productAttributes.orElse(null));
        //if(true) throw new RuntimeException("Transaction fail scenario");
        productCategoryRepository.saveAll(productCategories.orElse(null));
        return true;

    }

    public String generateSKU(String productName, String categoryName, String brandName) {
        String categoryCode = helper.getCodeFromString(categoryName, 2);
        String brandCode = helper.getCodeFromString(brandName, 2);
        String productNameCode = helper.getCodeFromString(productName, 3);

        int uniqueNumber = new Random().nextInt(9000) + 1000;

        String sku = categoryCode + "-" + brandCode + "-" + productNameCode + "-" + uniqueNumber;

        while (productRepository.existsBySku(sku)) {
            uniqueNumber = new Random().nextInt(9000) + 1000;
            sku = categoryCode + "-" + brandCode + "-" + productNameCode + "-" + uniqueNumber;
        }

        return sku;
    }

    public boolean deleteProductById(Integer id) {
        Product product = productRepository.findById(id).orElse(null);
        if(product == null) {
            throw new ProductNotFoundException("Product with not found", id.toString());
        }
        productRepository.delete(product);
        return true;
    }

    public List<Product> getProductByBrandId(Integer id) {
        return productRepository.findByBrand_Id(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with not found", id.toString()));
    }

    public List<Product> getProductByCategoryId(Integer id) {
        return productRepository.findByCategories_Id(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with not found", id.toString()));
    }

    public List<Product> getProductByAttributeId(Integer id) {
        return productRepository.findByAttributes_Id(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with not found", id.toString()));
    }

    public Product getProductBySku(String sku) {
        return productRepository.findOneBySku(sku)
                .orElseThrow(() -> new ProductNotFoundException("Product with not found with sku", sku));
    }

    @Transactional
    public String purchaseProductBySku(String sku) {
        Product product = getProductBySku(sku);
        product.setQuantity(product.getQuantity() - 1);
        productRepository.save(product);
        return "purchased";
    }
}
