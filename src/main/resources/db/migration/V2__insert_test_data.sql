INSERT INTO customer (customer_id, name) VALUES
('telco01', 'Liam Anderson'),
('telco02', 'Sophia Bennett'),
('telco03', 'Ethan Collins'),
('telco04', 'Olivia Turner'),
('telco05', 'Noah Mitchell'),
('telco06', 'Ava Thompson'),
('telco07', 'Mason Carter'),
('telco08', 'Isabella Wright'),
('telco09', 'James Parker'),
('telco10', 'Emily Hughes');

INSERT INTO phone_number (id, customer_id, number, is_active) VALUES
('550e8400-e29b-41d4-a716-446655440000', 'telco01', '+61412345678', TRUE),
('550e8400-e29b-41d4-a716-446655440001', 'telco01', '+61419876543', FALSE),
('550e8400-e29b-41d4-a716-446655440002', 'telco02', '+61422223333', TRUE),
('550e8400-e29b-41d4-a716-446655440003', 'telco03', '+61433334444', FALSE),
('550e8400-e29b-41d4-a716-446655440004', 'telco03', '+61435556666', TRUE),
('550e8400-e29b-41d4-a716-446655440005', 'telco05', '+61444445555', FALSE),
('550e8400-e29b-41d4-a716-446655440006', 'telco05', '+61446667777', TRUE),
('550e8400-e29b-41d4-a716-446655440007', 'telco07', '+61455556666', TRUE),
('550e8400-e29b-41d4-a716-446655440008', 'telco07', '+61459998888', FALSE),
('550e8400-e29b-41d4-a716-446655440009', 'telco10', '+61460001111', TRUE);