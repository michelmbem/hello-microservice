-- TABLES

CREATE TABLE category
(
    id UUID NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    CONSTRAINT pk_category PRIMARY KEY (id),
    CONSTRAINT un_category_name UNIQUE (name)
);

CREATE TABLE product
(
    id UUID NOT NULL,
    name VARCHAR(150) NOT NULL,
    unit_price DECIMAL(30, 10) NOT NULL,
    discounted_price DECIMAL(30, 10),
    category_id UUID NOT NULL,
    CONSTRAINT pk_product PRIMARY KEY (id),
    CONSTRAINT un_product_name UNIQUE (name)
);

CREATE TABLE product_image
(
    id UUID NOT NULL,
    url VARCHAR(255) NOT NULL,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    product_id UUID NOT NULL,
    CONSTRAINT pk_product_image PRIMARY KEY (id)
);

-- FOREIGN KEYS

ALTER TABLE product ADD CONSTRAINT fk_product_category
    FOREIGN KEY (category_id) REFERENCES category (id);

ALTER TABLE product_image ADD CONSTRAINT fk_product_image_product
    FOREIGN KEY (product_id) REFERENCES product (id) ON DELETE CASCADE ;
