package org.kash.jwtsecurityapi.controller;

import java.util.Optional;

import org.kash.jwtsecurityapi.dto.ProductDto;
import org.kash.jwtsecurityapi.mapper.ProductMapper;
import org.kash.jwtsecurityapi.model.Product;
import org.kash.jwtsecurityapi.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.websocket.server.PathParam;
import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/products")
@AllArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;
    
    @GetMapping
    public ResponseEntity<?> getAllProducts(){
        return ResponseEntity.ok(productMapper.toDtoList(productService.getAllProducts()));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable Long id){
        Optional<Product> productOptional = productService.getProduct(id);

        if(productOptional.isPresent()){
            return ResponseEntity.ok(productMapper.toDto(productOptional.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveProduct(@Valid @RequestBody ProductDto productDto) {
        var product = productMapper.fromDto(productDto);
        product.setId(null);
        return ResponseEntity.ok(productService.updateProduct(product));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDto productDto){
        var product = productMapper.fromDto(productDto);
        product.setId(id);
        return ResponseEntity.ok(productService.updateProduct(product));
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateProduct(@NotNull @PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted. ");
    }
}
