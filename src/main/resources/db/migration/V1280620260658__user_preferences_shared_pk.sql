
-- Drop constraint of foreign key for users table
ALTER TABLE users DROP CONSTRAINT IF EXISTS fk_users_preferences_table;

-- Update existing preferences id to user id
UPDATE user_preferences p SET id = u.id FROM users u where u.preferences_id = p.id;

-- Delete orphan preferences
DELETE FROM user_preferences p WHERE NOT EXISTS
    (SELECT 1 FROM users u WHERE u.id = p.id);

-- Delete relation too
ALTER TABLE users DROP COLUMN IF EXISTS preferences_id;

-- Preferences table new foreign table key
ALTER TABLE user_preferences
    ADD CONSTRAINT fk_preferences_users
    FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE;