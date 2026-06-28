CREATE TYPE THEME_MODE AS ENUM ('LIGHT', 'DARK', 'SYSTEM');
CREATE TYPE LANGUAGE_PREFERENCE AS ENUM ('AZ', 'EN', 'RU');

CREATE TABLE IF NOT EXISTS user_preferences (
    id                          UUID NOT NULL,
    timezone                    VARCHAR,
    language                    LANGUAGE_PREFERENCE NOT NULL DEFAULT 'EN',
    theme                       THEME_MODE NOT NULL DEFAULT 'SYSTEM',
    notification_enabled        BOOLEAN NOT NULL DEFAULT TRUE,
    email_notification_enabled  BOOLEAN NOT NULL DEFAULT TRUE,
    push_notification_enabled   BOOLEAN NOT NULL DEFAULT TRUE,
    updated_at                  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_at                  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_preferences PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT fk_users_preferences_table FOREIGN KEY (preferences_id) REFERENCES user_preferences(id);