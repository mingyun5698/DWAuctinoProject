package com.example.DWShopProject.service;

import com.example.DWShopProject.enums.ProductTypeEnum;
import com.example.DWShopProject.exception.ResourceNotFoundException;
import com.example.DWShopProject.dao.ProductDto;
import com.example.DWShopProject.entity.Product;
import com.example.DWShopProject.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductDto createProduct(ProductDto productDTO) {
        logger.info("Creating product with name: {}", productDTO.getProductName());
        validateImageUrl(productDTO.getImage()); // 이미지 URL 검증

        Product product = Product.builder()
                .productType(productDTO.getProductType())
                .productName(productDTO.getProductName())
                .price(productDTO.getPrice())
                .explanation(productDTO.getExplanation())
                .imageUrl(productDTO.getImage()) // 이미지 URL 필드 설정
                .createDate(LocalDateTime.now())
                .build();

        Product savedProduct = productRepository.save(product);
        logger.info("Product saved with ID: {}", savedProduct.getId());

        return mapToDTO(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductDto getProductById(Long id) {
        return productRepository.findById(id)
                .map(product -> new ProductDto(
                        product.getId(),
                        product.getProductType(),
                        product.getProductName(),
                        product.getPrice(),
                        product.getExplanation(),
                        product.getImageUrl()
                ))
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public Optional<ProductDto> getProductByName(String productName) {
        logger.info("Fetching product by name: {}", productName);
        return productRepository.findByProductName(productName)
                .map(this::mapToDTO);
    }

    @Transactional(readOnly = true)
    public List<ProductDto> getAllProducts() {
        logger.info("Fetching all products");
        return productRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public ProductDto updateProduct(Long id, ProductDto productDTO) {
        logger.info("Updating product with ID: {}", id);
        validateImageUrl(productDTO.getImage()); // 이미지 URL 검증

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("상품을 찾을 수 없습니다."));

        product.updateProductInfo(productDTO.getProductType(), productDTO.getProductName(),
                productDTO.getPrice(), productDTO.getExplanation(), productDTO.getImage());

        Product updatedProduct = productRepository.save(product);
        logger.info("Product updated with ID: {}", updatedProduct.getId());

        return mapToDTO(updatedProduct);
    }

    public void deleteProduct(Long id) {
        logger.info("Deleting product with ID: {}", id);
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("상품을 찾을 수 없습니다."));

        productRepository.delete(product);
        logger.info("Product deleted with ID: {}", id);
    }

    private ProductDto mapToDTO(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .productType(product.getProductType())
                .productName(product.getProductName())
                .price(product.getPrice())
                .explanation(product.getExplanation())
                .image(product.getImageUrl()) // 이미지 URL 필드 매핑
                .build();
    }

    private void validateImageUrl(String imageUrl) {
        if (imageUrl == null || !imageUrl.startsWith("https://")) {
            throw new IllegalArgumentException("Invalid image URL");
        }
    }

    public List<ProductDto> getProductsByCategory(Long categoryId) {
        ProductTypeEnum productType = ProductTypeEnum.getById(categoryId);
        List<Product> products = productRepository.findByProductType(productType);
        return products.stream()
                .map(product -> new ProductDto(
                        product.getId(),
                        product.getProductType(),
                        product.getProductName(),
                        product.getPrice(),
                        product.getExplanation(),
                        product.getImageUrl()
                ))
                .collect(Collectors.toList());
    }
}
