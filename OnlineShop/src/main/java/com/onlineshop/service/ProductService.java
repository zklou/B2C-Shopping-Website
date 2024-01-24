package com.onlineshop.service;

import com.onlineshop.Util;
import com.onlineshop.model.BrandEntity;
import com.onlineshop.model.ProductEntity;
import com.onlineshop.model.TypeEntity;
import com.onlineshop.repository.BrandRepository;
import com.onlineshop.repository.ProductRepository;
import com.onlineshop.repository.TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    @Autowired
    BrandRepository brandRepository;

    @Autowired
    TypeRepository typeRepository;

    @Autowired
    ProductRepository productRepository;

    public ResponseEntity<Object> getProduct(int productId) {
        ProductEntity productEntity = productRepository.getById(productId);

        if(productEntity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Util.buildGenericResponse(-1, "Not found :("));
        }

        return ResponseEntity.ok().body(Map.of(
                "id", productEntity.getId(),
                "name", productEntity.getTitle(),
                "brand", productEntity.getBrandId(),
                "type", productEntity.getTypeId(),
                "description", productEntity.getDetail(),
                "price", productEntity.getPrice(),
                "images", Arrays.asList(productEntity.getImages().split(","))
        ));
    }

    public ResponseEntity<Object> listProducts(int typeId, int brandId, int page) {
        List<ProductEntity> products;
        if(typeId != -1) {
            products = productRepository.findProductEntitiesByTypeId(typeId, PageRequest.of(page, 5));
        }
        else if(brandId != -1) {
            products = productRepository.findProductEntitiesByBrandId(brandId, PageRequest.of(page, 5));
        }
        else {
            Page<ProductEntity> productsPage = productRepository.findAll(PageRequest.of(page, 5));
            products = new ArrayList<>();
            for (var product: productsPage) {
                products.add(product);
            }
        }

        List<Map> result = new ArrayList<>();

        for (var product: products) {
            result.add(Map.of(
                    "id", product.getId(),
                    "name", product.getTitle(),
                    "price", product.getPrice(),
                    "images", Arrays.asList(product.getImages().split(","))
            ));
        }

        return ResponseEntity.ok().body(result);
    }


    public ResponseEntity<Object> listCategories() {
        var brands = brandRepository.findAll();
        var types = typeRepository.findAll();

        return ResponseEntity.accepted().body(Map.of(
                "brands", getCategoryList(brands),
                "types", getCategoryList(types)
        ));
    }

    List<String> getCategoryList(List items) {
        var result = new ArrayList<String>();

        for (Object o: items) {
            if(o instanceof BrandEntity) {
                result.add(String.format("%s::%d", ((BrandEntity) o).getName(), ((BrandEntity) o).getId()));
            }
            if(o instanceof TypeEntity) {
                result.add(String.format("%s::%d", ((TypeEntity) o).getName(), ((TypeEntity) o).getId()));
            }
        }

        return result;
    }

}
