package com.onlineshop.controller;

import com.onlineshop.service.ReviewService;
import lombok.Data;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class ReviewController {

    @Autowired
    ReviewService reviewService;

    @PutMapping("/product/{product_id}/review")
    ResponseEntity<Object> writeReview(@RequestBody ReviewRequest reviewContent, @PathVariable(name = "product_id") Integer productId, HttpServletRequest request) {
        return reviewService.sendReview(productId, reviewContent.rating, reviewContent.content, (int)request.getAttribute("UserID"));
    }

    @GetMapping("/product/{product_id}/review")
    ResponseEntity<Object> getReviews(@PathVariable(name = "product_id") Integer productId, HttpServletRequest request) {
        return reviewService.getReviews(productId, (int)request.getAttribute("UserID"));
    }

    @Getter
    @Data
    public static class ReviewRequest {
        Integer rating;
        String content;

    }

}
