package com.onlineshop.controller;

import com.onlineshop.service.OrderService;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/check_out")
    ResponseEntity<Object> checkOut(@RequestBody CheckOutRequest form, HttpServletRequest request) {
        return orderService.checkOut(
                form.credit_card_number,
                form.cvc,
                form.expiration_time,
                form.address,
                form.products,
                (int)request.getAttribute("UserID")
        );
    }

    @GetMapping("/orders")
    ResponseEntity<Object> getOrders(HttpServletRequest request) {
        return orderService.listOrders((int)request.getAttribute("UserID"), 0);
    }

    @GetMapping("/product_list/{product_list_id}")
    ResponseEntity<Object> getProductsOnList(@PathVariable(name = "product_list_id")Integer productListId, HttpServletRequest request) {
        return orderService.getProductsOnList(productListId, (int)request.getAttribute("UserID"));
    }

    @Data
    @Getter
    public static class CheckOutRequest {
        String credit_card_number;
        String cvc;
        String expiration_time;
        String address;
        List<Object> products;
    }

}
