package org.addy.productservice.mapper.decorator;

import lombok.extern.slf4j.Slf4j;
import org.addy.productservice.dto.ProductImageDto;
import org.addy.productservice.mapper.ProductImageMapper;
import org.addy.productservice.model.Product;
import org.addy.productservice.model.ProductImage;
import org.addy.productservice.repository.ProductRepository;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Slf4j
public abstract class ProductImageMapperDecorator implements ProductImageMapper {

    @Autowired
    @Qualifier("delegate")
    protected ProductImageMapper delegate;

    @Autowired
    private ProductRepository productRepository;

    @Override
    public ProductImageDto map(ProductImage image) {
        ProductImageDto imageDto = delegate.map(image);

        // MapStruct doesn't generate this
        imageDto.setDefault(image.isDefault());

        return imageDto;
    }

    @Override
    public ProductImage map(ProductImageDto imageDto) {
        ProductImage image = delegate.map(imageDto);
        mapProduct(imageDto, image);

        return image;
    }

    @Override
    public void map(ProductImageDto imageDto, @MappingTarget ProductImage image) {
        delegate.map(imageDto, image);
        mapProduct(imageDto, image);
    }

    private void mapProduct(ProductImageDto imageDto, ProductImage image) {
        // MapStruct doesn't generate this
        image.setDefault(imageDto.isDefault());

        if (imageDto.getProduct() != null) {
            Product product = productRepository.findById(imageDto.getProduct().getId()).orElseThrow();
            image.setProduct(product);
        }
    }
}
