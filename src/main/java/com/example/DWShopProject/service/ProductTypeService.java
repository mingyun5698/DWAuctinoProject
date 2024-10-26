package com.example.DWShopProject.service;

import com.example.DWShopProject.entity.ProductTypeMgmt;
import com.example.DWShopProject.enums.ParentTypeEnum;
import com.example.DWShopProject.enums.ProductTypeEnum;
import com.example.DWShopProject.repository.ProductTypeMgmtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductTypeService {
    private final ProductTypeMgmtRepository productTypeMgmtRepository;

    public List<ProductTypeMgmt> getAllProductTypes() {
        return productTypeMgmtRepository.findAll();
    }

    public Optional<ProductTypeMgmt> getProductType(ProductTypeEnum productType) {
        return productTypeMgmtRepository.findByProductType(productType);
    }

    public List<ProductTypeMgmt> getProductTypesByParent(ParentTypeEnum parentType) {
        return productTypeMgmtRepository.findByParentType(parentType);
    }
}
