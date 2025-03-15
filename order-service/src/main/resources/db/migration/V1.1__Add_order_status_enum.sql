-- CREATE ENUM

CREATE TYPE order_status AS
    ENUM('RECEIVED', 'CONFIRMED', 'PROCESSING', 'DELIVERING', 'DELIVERED', 'CANCELLED');

-- ALTER TABLE

ALTER TABLE "order"
    ADD COLUMN payment_method_id UUID,
    ADD COLUMN status order_status NOT NULL DEFAULT 'RECEIVED';
