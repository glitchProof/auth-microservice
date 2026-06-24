CREATE TABLE IF NOT EXISTS users (
    id              UUID NOT NULL,
    full_name       VARCHAR NOT NULL,
    username        VARCHAR NOT NULL,
    password_hash   VARCHAR,
    email           VARCHAR NOT NULL,
    email_verified  BOOLEAN DEFAULT false,
    google_sub_id   VARCHAR,
    preferences_id  UUID,
    provider        VARCHAR(50) NOT NULL,
    last_login      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    updated_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    created_at      TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

ALTER TABLE users ADD CONSTRAINT uc_users_email UNIQUE (email);
ALTER TABLE users ADD CONSTRAINT uc_users_google_sub_id UNIQUE (google_sub_id);
ALTER TABLE users ADD CONSTRAINT uc_users_username UNIQUE (username);
