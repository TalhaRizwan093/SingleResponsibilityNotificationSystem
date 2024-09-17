package com.toosterr.backend.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SaveProductRequest {

    @NotBlank(message = "Product Name is mandatory")
    private String name;
    @NotBlank(message = "Product Description is mandatory")
    private String description;
    @NotBlank(message = "Product detail is mandatory")
    private String detail;
    @Min(value = 1, message = "Product Price must be greater than 1")
    private double price;
    @Min(value = 1, message = "Product quantity must be greater than 1")
    private int quantity;
    private Integer brandId;
    @NotNull(message = "List of category must contain at least one element")
    @NotEmpty(message = "List of category must contain at least one element")
    @Size(min = 1, message = "List of category must contain at least one element")
    private List<@Positive(message = "Each integer must be positive") Integer> categoryList;
    @NotNull(message = "Product must have at least one attribute")
    @NotEmpty(message = "Product must have at least one attribute")
    @Size(min = 1, message = "Product must have at least one attribute")
    private List<@Positive(message = "Each integer must be positive") Integer> attributeList;

}
