INSERT INTO gift_certificates (name, description, price, create_date, last_update_date, duration)
VALUES ('Christmas Gift Certificate', 'This holiday with our gift certificate!', 50.00, NOW(), NOW(), 60);

INSERT INTO gift_certificates (name, description, price, create_date, last_update_date, duration)
VALUES ('Spa Gift Certificate', 'A day of pampering at our luxurious spa!', 100.00, NOW(), NOW(), 120);

INSERT INTO gift_certificates (name, description, price, create_date, last_update_date, duration)
VALUES ('Birthday Gift Certificate', 'Celebrate your special day with a gift certificate!', 75.00, NOW(), NOW(), 90);

INSERT INTO gift_certificates (name, description, price, create_date, last_update_date, duration)
VALUES ('Valentine', 'A gift certificate for Valentine Day', 49.99, NOW(), NOW(), 30);

INSERT INTO gift_certificates (name, description, price, create_date, last_update_date, duration)
VALUES ('Independence', 'A gift certificate for Independence Day', 29.99, NOW(), NOW(), 30);

INSERT INTO gift_certificates (name, description, price, create_date, last_update_date, duration)
VALUES ('Easter', 'A gift certificate for Easter', 39.99, NOW(), NOW(), 30);

INSERT INTO tag (name) VALUES ('Valentine');
INSERT INTO tag (name) VALUES ('Easter');
INSERT INTO tag (name) VALUES ('Christmas');
INSERT INTO tag (name) VALUES ('Spa');
INSERT INTO tag (name) VALUES ('Birthday');
INSERT INTO tag (name) VALUES ('Independence');
INSERT INTO tag (name) VALUES ('Spring');
INSERT INTO tag (name) VALUES ('Summer');
INSERT INTO tag (name) VALUES ('Weekend');
INSERT INTO tag (name) VALUES ('Holiday');

MERGE INTO gift_certificate_tag (gift_certificate_id, tag_id)
KEY (gift_certificate_id, tag_id)
VALUES (1, 3), (1, 5), (1, 1), (2, 4), (2, 2), (2, 5), (2, 3), (3, 5),
       (3, 1), (3, 2), (3, 1), (3, 4), (4, 1), (4, 4), (4, 5), (4, 3),
       (4, 1), (5, 4), (5, 5), (5, 5), (5, 4), (5, 3), (6, 2);