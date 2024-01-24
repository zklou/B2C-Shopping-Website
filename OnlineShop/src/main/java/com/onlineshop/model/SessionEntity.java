package com.onlineshop.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "session", schema = "finalproject", catalog = "")
public class SessionEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "cookies_session")
    private String cookiesSession;
    @Basic
    @Column(name = "user_id")
    private int userId;
    @Basic
    @Column(name = "expiration")
    private Timestamp expiration;

    public SessionEntity() {
        this.cookiesSession = UUID.randomUUID().toString();
        this.userId = -1;
        this.expiration = new Timestamp(System.currentTimeMillis() + 525600000);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SessionEntity that = (SessionEntity) o;
        return id == that.id && userId == that.userId && Objects.equals(cookiesSession, that.cookiesSession) && Objects.equals(expiration, that.expiration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cookiesSession, userId, expiration);
    }
}
