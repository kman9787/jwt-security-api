package org.kash.jwtsecurityapi.service;

import java.util.List;

import org.kash.jwtsecurityapi.model.Product;
import org.kash.jwtsecurityapi.repository.ProductRepository;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // Get all product
    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    // Get product by id
    public Product getProduct(Long id){
        return productRepository.findById(id).orElse(null);
    }

    // Save/Update product
    public Product updateProduct(Product product){
        return productRepository.save(product);
    }

    // Delete product
    public void deleteProduct(Long id){
        productRepository.deleteById(id);;
    }

}
