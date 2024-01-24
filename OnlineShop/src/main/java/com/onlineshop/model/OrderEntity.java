package com.onlineshop.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table(name = "`order`", schema = "finalproject", catalog = "")
public class OrderEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "user_id")
    private int userId;
    @Basic
    @CreationTimestamp
    @Column(name = "order_time")
    private Date orderTime;
    @Basic
    @Column(name = "product_list_id")
    private int productListId;
    @Basic
    @Column(name = "address_id")
    private int addressId;
    @Basic
    @Column(name = "payment_id")
    private int paymentId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderEntity that = (OrderEntity) o;
        return id == that.id && userId == that.userId && productListId == that.productListId && addressId == that.addressId && paymentId == that.paymentId && orderTime.equals(that.orderTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, orderTime, productListId, addressId, paymentId);
    }
}
