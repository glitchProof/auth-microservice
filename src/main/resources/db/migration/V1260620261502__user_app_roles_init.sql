CREATE TABLE IF NOT EXISTS user_app_roles (
    id          UUID NOT NULL,
    user_id     UUID NOT NULL,
    app         VARCHAR(70) NOT NULL,
    role        VARCHAR(50) NOT NULL,
    CONSTRAINT  pk_user_app_roles           PRIMARY KEY (id),
    CONSTRAINT  uq_user_app_roles           UNIQUE (user_id, app, role),
    CONSTRAINT  fk_user_app_roles_to_user   FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_user_app_role_user ON user_app_roles(user_id);