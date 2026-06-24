CREATE TABLE IF NOT EXISTS user_preferences (
    id                          UUID NOT NULL,
    timezone                    VARCHAR,
    language                    VARCHAR NOT NULL DEFAULT 'EN',
    theme                       VARCHAR NOT NULL DEFAULT 'SYSTEM',
    notification_enabled        BOOLEAN NOT NULL DEFAULT TRUE,
    email_notification_enabled  BOOLEAN NOT NULL DEFAULT TRUE,
    push_notification_enabled   BOOLEAN NOT NULL DEFAULT TRUE,
    updated_at                  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_at                  TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_preferences PRIMARY KEY (id)
);

ALTER TABLE users
    ADD CONSTRAINT fk_users_preferences_table FOREIGN KEY (preferences_id) REFERENCES user_preferences(id);