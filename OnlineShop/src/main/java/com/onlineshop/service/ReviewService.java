package com.onlineshop.service;

import com.onlineshop.model.ReviewEntity;
import com.onlineshop.repository.ProductRepository;
import com.onlineshop.repository.ReviewRepository;
import com.onlineshop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ReviewService {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<Object> sendReview(int productId, int rating, String content, int userId) {
        var reviewEntity = new ReviewEntity();
        reviewEntity.setUserId(userId);
        reviewEntity.setRating((short)(rating * 2));
        reviewEntity.setContent(content);
        reviewEntity.setProductId(productId);

        reviewRepository.save(reviewEntity);

        return ResponseEntity.ok(Map.of(
                "id", 0,
                "message", "Success"
        ));
    }

    public ResponseEntity<Object> getReviews(int productId, int userId) {
        var reviews = reviewRepository.findReviewEntitiesByProductId(productId);
        int total = 0;
        int count = 0;

        List<Map> reviewsField = new ArrayList<>();

        for (var review: reviews) {
            total++;
            count += review.getRating();

            var userEntity = userRepository.findById(review.getUserId()).get();

            reviewsField.add(Map.of(
                    "display_name", userEntity.getFirstName() + " " + userEntity.getLastName(),
                    "date", review.getDate().getTime() + "",
                    "rating", review.getRating() / 2.0,
                    "content", review.getContent()
            ));

        }

        return ResponseEntity.ok(Map.of(
                "average_rating", total / 2.0 / count,
                "reviews", reviewsField
        ));

    }


}
