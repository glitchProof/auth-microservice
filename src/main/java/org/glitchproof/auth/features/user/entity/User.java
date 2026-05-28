package org.glitchproof.auth.features.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name= "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @UuidGenerator
    String id;

    @Column(
            name = "full_name",
            nullable = false
    )
    String fullName;

    @Column(nullable = false)
    String username;

    @Column(name="password_hash",nullable = false)
    String passwordHash;

    @Column(unique = true, nullable = false)
    String email;


    @Column(
            name = "last_login",
            nullable = false
    )
    LocalDateTime lastLogin;

    @Column(
            name= "updated_at",
            nullable = false
    )
    @UpdateTimestamp
    LocalDateTime updatedAt;


    @Column(
            name= "created_at",
            nullable = false
    )
    @CreationTimestamp
    LocalDateTime createdAt;
}
