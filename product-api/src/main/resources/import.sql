INSERT INTO category (description) VALUES ('carro');
INSERT INTO category (description) VALUES ('moto');

INSERT INTO supplier (name) VALUES ('FIAT');
INSERT INTO supplier (name) VALUES ('HONDA');

INSERT INTO product (created_at, name, quantity_available, fk_category, fk_supplier) VALUES (now(), 'uno', 10, 1, 1);
INSERT INTO product (created_at, name, quantity_available, fk_category, fk_supplier) VALUES (now(), 'CG 125', 10, 2, 2);