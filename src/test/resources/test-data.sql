DROP TABLE IF EXISTS phone_number;
DROP TABLE IF EXISTS customer;

CREATE TABLE IF NOT EXISTS customer (
    customer_id VARCHAR(100) PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS phone_number (
    id UUID PRIMARY KEY,
    number VARCHAR(20) NOT NULL,
    is_active BOOLEAN NOT NULL,
    customer_id VARCHAR(100) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

INSERT INTO customer (customer_id, name)
VALUES ('cust123', 'Naruto'),
    ('cust456', 'Nupur S'),
    ('cust000', 'James Bond');

INSERT INTO phone_number (id, customer_id, number, is_active)
VALUES ('444f9bfe-3d40-46a1-8d13-ac6770f8b64e','cust123', '1234567890', false),
    ('555f9bfe-3d40-46a1-8d13-ac6770f8b64e','cust123', '9876543210', true),
    ('666f9bfe-3d40-46a1-8d13-ac6770f8b64e', 'cust456', '5556665556', false);
