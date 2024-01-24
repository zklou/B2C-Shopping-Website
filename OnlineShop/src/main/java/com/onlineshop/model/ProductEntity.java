package com.onlineshop.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;



@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "product", schema = "finalproject", catalog = "")
public class ProductEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "title")
    private String title;
    @Basic
    @Column(name = "detail")
    private String detail;
    @Basic
    @Column(name = "price")
    private int price;
    @Basic
    @Column(name = "images")
    private String images;

    @Basic
    @Column(name = "brand_id")
    private int brandId;

    @Basic
    @Column(name = "type_id")
    private int typeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductEntity that = (ProductEntity) o;
        return id == that.id && price == that.price && Objects.equals(title, that.title) && Objects.equals(detail, that.detail) && Objects.equals(images, that.images);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, detail, price, images);
    }
}
