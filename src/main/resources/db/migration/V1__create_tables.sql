CREATE TABLE customer (
    customer_id VARCHAR(100) PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE phone_number (
    id UUID PRIMARY KEY,
    number VARCHAR(20) NOT NULL,
    is_active BOOLEAN NOT NULL,
    customer_id VARCHAR(100) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

