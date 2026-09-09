package org.kash.jwtsecurityapi.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductDto(
    Long id, 
    @NotBlank String name, 
    @NotNull BigDecimal price) {
}
