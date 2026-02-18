INSERT INTO users (id, name, email, password, created, modified, is_active)
VALUES (RANDOM_UUID(), 'Santi Cabo', 'santi.cabo@domain.org', 'hashed-password', NOW(), NOW(), TRUE);