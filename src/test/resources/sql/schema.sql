CREATE TABLE member
(
    member_id       binary(16)  NOT NULL,
    name            varchar(20) NOT NULL,
    address         varchar(50) NOT NULL,
    address_details varchar(30) NOT NULL,
    zipcode         int         NOT NULL,
    created_at      datetime(6) NOT NULL,
    PRIMARY KEY (member_id)
);

CREATE TABLE brand
(
    brand_id        binary(16)  NOT NULL,
    name            varchar(30) NOT NULL,
    address         varchar(50) NOT NULL,
    address_details varchar(30) NOT NULL,
    zipcode         int         NOT NULL,
    created_at      datetime(6) NOT NULL,
    PRIMARY KEY (brand_id)
);

CREATE TABLE orders
(
    order_id        binary(16)  NOT NULL,
    status          varchar(15) NOT NULL,
    address         varchar(50) NOT NULL,
    address_details varchar(30) NOT NULL,
    zipcode         int         NOT NULL,
    created_at      datetime(6) NOT NULL,
    member_id       binary(16)  NOT NULL,
    PRIMARY KEY (order_id),
    KEY fk_orders_member_idx (member_id),
    CONSTRAINT fk_orders_member FOREIGN KEY (member_id) REFERENCES member (member_id)
);

CREATE TABLE product
(
    product_id     binary(16)  NOT NULL,
    name           varchar(30) NOT NULL,
    price          int         NOT NULL,
    category       varchar(20) NOT NULL,
    created_at     datetime(6) NOT NULL,
    brand_id       binary(16)  NOT NULL,
    stock_quantity int         NOT NULL default 0,
    PRIMARY KEY (product_id),
    KEY fk_product_brand1_idx (brand_id),
    CONSTRAINT fk_product_brand1 FOREIGN KEY (brand_id) REFERENCES brand (brand_id)
);

CREATE TABLE order_product_list
(
    order_product_id binary(16) NOT NULL,
    order_id         binary(16) NOT NULL,
    product_id       binary(16) NOT NULL,
    order_price      int        NOT NULL,
    count            int        NOT NULL,
    PRIMARY KEY (order_product_id),
    KEY fk_order_product_list_product1_idx (product_id),
    KEY fk_order_product_list_orders1 (order_id),
    CONSTRAINT fk_order_product_list_orders1 FOREIGN KEY (order_id) REFERENCES orders (order_id),
    CONSTRAINT fk_order_product_list_product1 FOREIGN KEY (product_id) REFERENCES product (product_id)
);
