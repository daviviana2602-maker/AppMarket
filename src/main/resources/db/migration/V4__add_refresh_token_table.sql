CREATE TABLE refresh_tokens (
                                id BIGSERIAL PRIMARY KEY,
                                user_id BIGINT NOT NULL,
                                token_hash VARCHAR(255) NOT NULL UNIQUE,
                                expires_at TIMESTAMP NOT NULL,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                CONSTRAINT fk_refresh_token_user
                                    FOREIGN KEY (user_id)
                                        REFERENCES users(id)
);