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


INSERT INTO gift_certificate_tag (gift_certificate_id, tag_id)
VALUES (1, 3), (2, 4), (3, 5), (4, 1), (5, 3), (6, 2);