package com.onlineshop.model;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.sql.Date;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "api_access", schema = "finalproject", catalog = "")
public class ApiAccessEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Basic
    @Column(name = "path")
    private String path;

    @Basic
    @Column(name = "time")
    @CreationTimestamp
    private Date time;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiAccessEntity that = (ApiAccessEntity) o;
        return id == that.id && path.equals(that.path) && time.equals(that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, path, time);
    }

}
