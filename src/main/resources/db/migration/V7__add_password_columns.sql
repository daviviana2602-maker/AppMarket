ALTER TABLE users
    ADD COLUMN password_reset_token VARCHAR(255),
    ADD COLUMN password_reset_expires_at TIMESTAMP,
    ADD COLUMN new_password VARCHAR(255);