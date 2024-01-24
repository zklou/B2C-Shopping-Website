package com.onlineshop.controller;

import com.onlineshop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/categories")
    ResponseEntity<Object> categories() {
        return productService.listCategories();
    }

    @GetMapping(value = {"/products/type/{type_id}/page/{page_number}", "/products/type/{type_id}"})
    ResponseEntity<Object> getProductsByType(@PathVariable(name = "type_id")Integer typeId, @PathVariable(name = "page_number", required = false)Integer pageNumber) {
        if(pageNumber == null) {
            pageNumber = 0;
        }

        return productService.listProducts(typeId, -1, pageNumber);
    }

    @GetMapping(value = {"/products/brand/{brand_id}/page/{page_number}", "/products/brand/{brand_id}"})
    ResponseEntity<Object> getProductsByBrand(@PathVariable(name = "brand_id")Integer brandId, @PathVariable(name = "page_number", required = false)Integer pageNumber) {
        if(pageNumber == null) {
            pageNumber = 0;
        }

        return productService.listProducts(-1, brandId, pageNumber);
    }

    @GetMapping(value = {"/products", "/products/page/{page_number}"})
    ResponseEntity<Object> getProducts(@PathVariable(name = "page_number", required = false)Integer pageNumber) {
        if(pageNumber == null) {
            pageNumber = 0;
        }

        return productService.listProducts(-1, -1, pageNumber);
    }

    @GetMapping(value = {"/product/{product_id}" })
    ResponseEntity<Object> getProduct(@PathVariable(name = "product_id")Integer productId) {

        return productService.getProduct(productId);
    }

}
