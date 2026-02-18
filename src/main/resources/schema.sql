CREATE TABLE users (
                       id UUID PRIMARY KEY,
                       name VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       created TIMESTAMP NOT NULL,
                       modified TIMESTAMP NOT NULL,
                       last_login TIMESTAMP,
                       token VARCHAR(512),
                       is_active BOOLEAN NOT NULL
);

CREATE TABLE phones (
                        id UUID PRIMARY KEY,
                        user_id UUID NOT NULL,
                        number VARCHAR(20) NOT NULL,
                        city_code VARCHAR(10),
                        country_code VARCHAR(10),
                        CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_phones_user_id ON phones(user_id);