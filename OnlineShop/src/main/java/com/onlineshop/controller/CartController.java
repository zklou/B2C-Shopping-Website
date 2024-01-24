package com.onlineshop.controller;

import com.onlineshop.service.CartService;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CartController {

    @Autowired
    CartService cartService;

    @GetMapping("/cart")
    ResponseEntity<Object> listCart(HttpServletRequest request) {
        return cartService.listCart((int)request.getAttribute("UserID"));
    }

    @PutMapping("/cart")
    ResponseEntity<Object> addToCart(@RequestBody CartRequest form, HttpServletRequest request) {
        return cartService.putToCart(form.getProduct_id(), (int)request.getAttribute("UserID"));
    }

    @DeleteMapping("/cart/{product_id}")
    ResponseEntity<Object> removeFromCart(@PathVariable(name = "product_id") int product_id, HttpServletRequest request) {
        return cartService.removeFromCart(product_id, (int)request.getAttribute("UserID"));
    }

    @PostMapping("/cart/clear")
    ResponseEntity<Object> clearCart(HttpServletRequest request) {
        return cartService.clearCart((int)request.getAttribute("UserID"));
    }

    @Getter
    @Data
    public static class CartRequest {
        int product_id;
    }

}
