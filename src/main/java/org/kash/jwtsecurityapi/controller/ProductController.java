package org.kash.jwtsecurityapi.controller;

import org.kash.jwtsecurityapi.mapper.ProductMapper;
import org.kash.jwtsecurityapi.service.ProductService;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;
    
    @GetMapping
    public ResponseEntity<?> getAllProducts(){
        var productMapper = Mappers.getMapper(ProductMapper.class);
        return ResponseEntity.ok(productMapper.toDtoList(productService.getAllProducts()));
    }
}
