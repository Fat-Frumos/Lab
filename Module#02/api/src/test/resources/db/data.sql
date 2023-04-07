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

INSERT INTO tag (id, name)
VALUES (1, 'Valentine');
INSERT INTO tag (id, name)
VALUES (2, 'Easter');
INSERT INTO tag (id, name)
VALUES (3, 'Christmas');
INSERT INTO tag (id, name)
VALUES (4, 'Spa');
INSERT INTO tag (id, name)
VALUES (5, 'Birthday');
INSERT INTO tag (id, name)
VALUES (115, 'Independence');
INSERT INTO tag (id, name)
VALUES (113, 'Spring');
INSERT INTO tag (id, name)
VALUES (114, 'Summer');
INSERT INTO tag (id, name)
VALUES (111, 'Weekend');
INSERT INTO tag (id, name)
VALUES (112, 'Holiday');

INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (1, 3);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (1, 115);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (1, 1);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (2, 4);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (2, 2);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (2, 5);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (2, 113);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (3, 5);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (3, 111);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (3, 112);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (3, 1);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (3, 4);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (4, 1);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (4, 4);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (4, 5);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (4, 113);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (4, 111);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (5, 4);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (5, 5);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (5, 115);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (5, 114);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (5, 113);
INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id) VALUES (6, 2);
