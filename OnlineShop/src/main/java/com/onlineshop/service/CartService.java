package com.onlineshop.service;

import com.onlineshop.model.CartEntity;
import com.onlineshop.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    @Autowired
    CartRepository cartRepository;

    public ResponseEntity<Object> putToCart(int productId, int userId) {
        var cart = getCart(userId);

        if(cart == null) {
            var newCart = new CartEntity();
            newCart.setUserId(userId);
            newCart.setItems("");
            newCart = cartRepository.save(newCart);
            cart = newCart;
        }

        var productIds = cart.getItems();
        if(productIds.length() == 0) {
            productIds += productId;
        }
        else {
            productIds += ",";
            productIds += productId;
        }
        cart.setItems(productIds);

        return ResponseEntity.ok(Map.of("products", Arrays.asList(cartRepository.save(cart).getItems().split(","))));
    }

    public ResponseEntity<Object> listCart(int userId) {
        var cart = getCart(userId);

        if(cart == null) {
            return ResponseEntity.ok(Map.of("products", new ArrayList()));
        }

        return ResponseEntity.ok(Map.of("products", Arrays.asList(cart.getItems().split(","))));
    }

    public ResponseEntity<Object> removeFromCart(int productId, int userId) {
        var cart = getCart(userId);

        if(cart == null) {
            return ResponseEntity.ok(Map.of("products", new ArrayList()));
        }

        var products = new String[0];

        if(cart.getItems().length() > 0) {
            products = cart.getItems().split(",");
        }

        var productItems = new ArrayList(Arrays.asList(products));

        productItems.remove(String.valueOf(productId));

        cart.setItems(String.join(",", productItems));

        cartRepository.save(cart);

        return ResponseEntity.ok(Map.of("products",productItems));

    }

    public ResponseEntity<Object> clearCart(int userId) {

        var cart = getCart(userId);

        if(cart == null) {
            return ResponseEntity.ok(Map.of("products", new ArrayList()));
        }

        cart.setItems("");

        cartRepository.save(cart);

        return ResponseEntity.ok(Map.of(
                "id", 0,
                "message", "OK"
        ));
    }

    CartEntity getCart(int userId) {
        var carts = cartRepository.findCartEntitiesByUserIdOrderById(userId);
        if(carts.size() == 0) {
            return null;
        }

        if(carts.size() > 1) {
            System.err.println("[+] Warning: One more carts");
        }

        return carts.get(0);
    }


}
