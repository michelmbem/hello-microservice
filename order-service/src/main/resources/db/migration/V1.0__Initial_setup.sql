-- TABLES

CREATE TABLE "order"
(
    id UUID NOT NULL,
    created_on timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    customer_id CHAR(24) NOT NULL,
    CONSTRAINT pk_order PRIMARY KEY (id)
);

CREATE TABLE order_item
(
    id UUID NOT NULL,
    order_id UUID NOT NULL,
    product_id UUID NOT NULL,
    unit_price DECIMAL(30, 10) NOT NULL,
    quantity SMALLINT NOT NULL,
    discount REAL NOT NULL DEFAULT 0,
    rank SMALLINT NOT NULL,
    CONSTRAINT pk_order_item PRIMARY KEY (id)
);

-- FOREIGN KEYS

ALTER TABLE order_item ADD CONSTRAINT fk_order_item_order
    FOREIGN KEY (order_id) REFERENCES "order" (id) ON DELETE CASCADE ;
