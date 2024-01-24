package com.onlineshop.model;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "ordered_item", schema = "finalproject", catalog = "")
public class OrderedItemEntity {
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
    @Column(name = "product_list_id")
    private int productListId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getProductListId() {
        return productListId;
    }

    public void setProductListId(int productListId) {
        this.productListId = productListId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderedItemEntity that = (OrderedItemEntity) o;
        return id == that.id && userId == that.userId && productId == that.productId && productListId == that.productListId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, productId, productListId);
    }
}
