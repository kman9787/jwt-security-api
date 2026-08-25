package org.kash.jwtsecurityapi.repository;

import org.kash.jwtsecurityapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>{
}
