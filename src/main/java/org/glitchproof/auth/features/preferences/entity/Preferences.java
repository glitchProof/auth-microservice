package org.glitchproof.auth.features.preferences.entity;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.type.SqlTypes;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.CreationTimestamp;
import org.glitchproof.auth.features.user.entity.User;
import org.glitchproof.auth.features.preferences.enums.Languages;
import org.glitchproof.auth.features.preferences.enums.ThemeMode;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_preferences")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Preferences {
    @Id
    UUID id;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(value = SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "language_preference")
    Languages language = Languages.EN;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(value = SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "theme_mode")
    ThemeMode theme = ThemeMode.SYSTEM;

    String timezone;

    @Builder.Default
    Boolean notificationEnabled = true;

    @Builder.Default
    Boolean emailNotificationEnabled = true;

    @Builder.Default
    Boolean pushNotificationEnabled = true;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    User user;

    @UpdateTimestamp
    LocalDateTime updatedAt;

    @CreationTimestamp
    LocalDateTime createdAt;
}
