-- ALTER TABLE

ALTER TABLE product DROP CONSTRAINT fk_product_category;
ALTER TABLE product DROP COLUMN category_id;

-- CREATE TABLE

CREATE TABLE product_category
(
    product_id UUID NOT NULL,
    category_id UUID NOT NULL,
    CONSTRAINT pk_product_category PRIMARY KEY (product_id, category_id)
);

-- FOREIGN KEYS

ALTER TABLE product_category ADD CONSTRAINT fk_product_category_product
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE;

ALTER TABLE product_category ADD CONSTRAINT fk_product_category_category
    FOREIGN KEY (category_id) REFERENCES category (id);
