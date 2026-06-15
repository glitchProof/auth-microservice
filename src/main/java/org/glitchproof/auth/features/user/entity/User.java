package org.glitchproof.auth.features.user.entity;

import lombok.*;
import jakarta.persistence.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.CreationTimestamp;
import org.glitchproof.auth.features.user.enums.AuthProvider;

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

    @Column(nullable = false, unique = true)
    String username;

    @ToString.Exclude
    @Column(name="password_hash")
    String passwordHash;

    @Column(unique = true, nullable = false)
    String email;

    @Column(unique = true)
    String googleSubId;

    @Enumerated(EnumType.STRING)
    AuthProvider provider;

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
