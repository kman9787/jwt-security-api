package org.kash.jwtsecurityapi.mapper;

import java.util.List;
import java.util.Optional;

import org.kash.jwtsecurityapi.dto.ProductDto;
import org.kash.jwtsecurityapi.model.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    //Optional<ProductDto> toDto(Optional<Product> product);
    ProductDto toDto(Product product);
    Product fromDto(ProductDto productDto);
    List<ProductDto> toDtoList(List<Product> products);
    List<Product> fromDtoList(List<ProductDto> productDtos);

}
