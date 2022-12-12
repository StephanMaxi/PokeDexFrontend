package com.example.demo.registration.token;

import java.time.LocalDateTime;

import com.example.demo.Models.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class ConfirmationToken {
    


    @SequenceGenerator(
        name = "confirmation_sequence",
        sequenceName = "confirmation_token_sequence",
        allocationSize = 1
    )
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "confirmation_token_sequence"
    )
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime expiredAt;

    private LocalDateTime comfirmedAt;
    // a user can have many email tokens
    @ManyToOne
    @JoinColumn(nullable = false,
                name = "user_id")
    private User user;



    
    public ConfirmationToken() {
    }

    
    public ConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiredAt,User user ) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiredAt = expiredAt;
        this.user = user;
    }


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }
    public void setExpiredat(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }
    public LocalDateTime getComfirmedAt() {
        return comfirmedAt;
    }
    public void setComfirmedAt(LocalDateTime comfirmedAt) {
        this.comfirmedAt = comfirmedAt;
    }


    public User getUser() {
        return user;
    }


    
}
