package com.toosterr.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddAttributeRequest {

    @NotBlank(message = "Attribute name cannot be blank")
    private String name;
    @NotBlank(message = "Attribute type cannot be blank")
    private String type;
    @NotBlank(message = "Attribute categoryId cannot be blank")
    private Integer categoryId;

}
