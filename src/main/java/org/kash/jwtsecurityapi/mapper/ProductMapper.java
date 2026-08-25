package org.kash.jwtsecurityapi.mapper;

import java.util.List;

import org.kash.jwtsecurityapi.dto.ProductDto;
import org.kash.jwtsecurityapi.model.Product;
import org.mapstruct.Mapper;

@Mapper
public interface ProductMapper {
    ProductDto toDto(Product product);
    Product fromDto(ProductDto productDto);
    List<ProductDto> toDtoList(List<Product> products);
    List<Product> fromDtoList(List<ProductDto> productDtos);

}
