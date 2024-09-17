package com.toosterr.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddBrandRequest {

    @NotBlank(message = "Brand name cannot be blank")
    private String name;
    @NotBlank(message = "Brand description cannot be blank")
    private String description;

}
