package org.glitchproof.auth.features.preferences.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.glitchproof.auth.features.preferences.enums.Languages;
import org.glitchproof.auth.features.preferences.enums.ThemeMode;
import org.glitchproof.auth.features.user.entity.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_preferences")
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Preferences {
    @Id
    @UuidGenerator
    UUID id;

    @Enumerated(EnumType.STRING)
    Languages language = Languages.EN;

    @Enumerated(EnumType.STRING)
    ThemeMode theme = ThemeMode.SYSTEM;

    String timezone;

    @Column(name = "notification_enabled")
    Boolean notificationEnabled = true;

    @Column(name = "email_notification_enabled")
    Boolean emailNotificationEnabled = true;

    @Column(name = "push_notification_enabled")
    Boolean pushNotificationEnabled = true;

    @OneToOne(mappedBy = "preferences")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    User user;

    @Column(name = "updated_at")
    @UpdateTimestamp
    LocalDateTime updateAt;

    @Column(name = "created_at")
    @CreationTimestamp
    LocalDateTime createAt;
}
