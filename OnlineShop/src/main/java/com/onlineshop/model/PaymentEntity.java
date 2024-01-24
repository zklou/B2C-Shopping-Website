package com.onlineshop.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "payment", schema = "finalproject", catalog = "")
public class PaymentEntity {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentEntity that = (PaymentEntity) o;
        return id == that.id && creditCardNumber.equals(that.creditCardNumber) && cvc.equals(that.cvc) && expirationTime.equals(that.expirationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, creditCardNumber, cvc, expirationTime);
    }

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "credit_card_number")
    private String creditCardNumber;

    @Basic
    @Column(name = "cvc")
    private String cvc;

    @Basic
    @Column(name = "expiration_time")
    private String expirationTime;

    @Basic
    @Column(name = "user_id")
    private Integer userId;

}
