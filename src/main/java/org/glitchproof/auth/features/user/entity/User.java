package org.glitchproof.auth.features.user.entity;

import lombok.*;

import java.util.UUID;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.experimental.FieldDefaults;
import org.glitchproof.auth.features.preferences.entity.Preferences;
import org.glitchproof.auth.features.user.enums.PlatformRole;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.CreationTimestamp;
import org.glitchproof.auth.features.user.enums.AuthProvider;
import org.hibernate.type.SqlTypes;


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

    @Column(nullable = false)
    String fullName;

    @Column(nullable = false, unique = true)
    String username;

    @ToString.Exclude
    String passwordHash;

    @Column(unique = true, nullable = false)
    String email;

    @Column( nullable = false)
    @Builder.Default
    Boolean emailVerified = false;

    @Column(unique = true)
    String googleSubId;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(value = SqlTypes.NAMED_ENUM)
    @Column(
            nullable = false,
            length = 50,
            columnDefinition = "user_login_provider"
    )
    AuthProvider provider;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(
            nullable = false,
            length = 50,
            columnDefinition = "user_platform_role"
    )
    PlatformRole role = PlatformRole.USER;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Preferences preferences;

    @Column(
            nullable = false
    )
    LocalDateTime lastLogin;

    @Column(
            nullable = false
    )
    @UpdateTimestamp
    LocalDateTime updatedAt;

    @Column(
            nullable = false
    )
    @CreationTimestamp
    LocalDateTime createdAt;

    @PrePersist
    void initPreferences(){
        if(preferences == null){
            preferences = new Preferences();
        }
        // For getting user id for @MapsId
        preferences.setUser(this);
    }


    public Boolean canUseMagicLinkLogin(){
        return this.emailVerified;
    }
}
