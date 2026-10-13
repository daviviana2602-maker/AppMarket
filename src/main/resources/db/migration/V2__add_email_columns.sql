ALTER TABLE users
    ADD COLUMN email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN email_verification_token VARCHAR(255),
    ADD COLUMN email_verification_expires_in TIMESTAMP;