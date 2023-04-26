DELETE FROM gift_certificates;

INSERT INTO gift_certificates (id, name, description, price, create_date, last_update_date, duration)
VALUES (1, 'Christmas Gift Certificate', 'This holiday with our gift certificate!', 50.00, NOW(), NOW(), 60);

INSERT INTO gift_certificates (id, name, description, price, create_date, last_update_date, duration)
VALUES (2, 'Spa Gift Certificate', 'A day of pampering at our luxurious spa!', 100.00, NOW(), NOW(), 120);

INSERT INTO gift_certificates (id, name, description, price, create_date, last_update_date, duration)
VALUES (3, 'Birthday Gift Certificate', 'Celebrate your special day with a gift certificate!', 75.00, NOW(), NOW(), 90);

INSERT INTO gift_certificates (id, name, description, price, create_date, last_update_date, duration)
VALUES (4, 'Valentine', 'A gift certificate for Valentine Day', 49.99, NOW(), NOW(), 30);

INSERT INTO gift_certificates (id, name, description, price, create_date, last_update_date, duration)
VALUES (5, 'Independence', 'A gift certificate for Independence Day', 29.99, NOW(), NOW(), 30);

INSERT INTO gift_certificates (id, name, description, price, create_date, last_update_date, duration)
VALUES (6, 'Easter', 'A gift certificate for Easter', 39.99, NOW(), NOW(), 30);

DELETE FROM tag;

INSERT INTO tag (id, name) VALUES (1, 'Valentine');
INSERT INTO tag (id, name) VALUES (2, 'Easter');
INSERT INTO tag (id, name) VALUES (3, 'Christmas');
INSERT INTO tag (id, name) VALUES (4, 'Spa');
INSERT INTO tag (id, name) VALUES (5, 'Birthday');
INSERT INTO tag (id, name) VALUES (115, 'Independence');
INSERT INTO tag (id, name) VALUES (113, 'Spring');
INSERT INTO tag (id, name) VALUES (114, 'Summer');
INSERT INTO tag (id, name) VALUES (111, 'Weekend');
INSERT INTO tag (id, name) VALUES (112, 'Holiday');
INSERT INTO tag (id, name) VALUES (116, 'Anniversary');

MERGE INTO gift_certificate_tag (gift_certificate_id, tag_id)
KEY (gift_certificate_id, tag_id)
VALUES (1, 3), (1, 5), (1, 1), (2, 4), (2, 2), (2, 5), (2, 3), (3, 5),
       (3, 1), (3, 2), (3, 1), (3, 4), (4, 1), (4, 4), (4, 5), (4, 3),
       (4, 1), (5, 4), (5, 5), (5, 5), (5, 4), (5, 3), (6, 2);