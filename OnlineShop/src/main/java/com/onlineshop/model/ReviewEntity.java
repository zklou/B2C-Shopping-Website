package com.onlineshop.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "review", schema = "finalproject", catalog = "")
public class ReviewEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "user_id")
    private int userId;

    @Basic
    @Column(name = "product_id")
    private int productId;

    @Basic
    @Column(name = "content")
    private String content;

    @Basic
    @Column(name = "rating")
    private short rating;

    @Basic
    @Column(name = "date")
    @CreationTimestamp
    private Timestamp date;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReviewEntity that = (ReviewEntity) o;
        return id == that.id && userId == that.userId && productId == that.productId && rating == that.rating && content.equals(that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, productId, content, rating);
    }
}
