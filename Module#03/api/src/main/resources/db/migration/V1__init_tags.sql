DROP TABLE IF EXISTS tags;

CREATE TABLE tags
(
    tag_id   SERIAL PRIMARY KEY,
    name VARCHAR(55) NOT NULL
);


DROP TABLE IF EXISTS gift_certificate_tag;

CREATE TABLE gift_certificate_tag
(
    gift_certificate_id BIGINT REFERENCES gift_certificates (id),
    tag_id              BIGINT REFERENCES tags (tag_id),
    PRIMARY KEY (gift_certificate_id, tag_id)
);
