package org.glitchproof.auth.features.user.entity;

import lombok.*;
import java.util.UUID;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.experimental.FieldDefaults;
import org.glitchproof.auth.features.preferences.entity.Preferences;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.CreationTimestamp;
import org.glitchproof.auth.features.user.enums.AuthProvider;


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
    UUID id;

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

    @Column( nullable = false)
    Boolean emailVerified = false;

    @Column(unique = true)
    String googleSubId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    AuthProvider provider;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "preferences_id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Preferences preferences;

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

    @PrePersist
    void initPreferences(){
        if(preferences == null){
            preferences = new Preferences();
        }
    }
}
