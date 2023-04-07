package com.epam.esm.mapper;

public final class QueriesContext {
    private QueriesContext() {
    }

    public static final String FIELDS = "id, name, description, price, create_date, last_update_date, duration";
    public static final String SELECT = "SELECT c.id, c.name, c.description, c.price, c.create_date, c.last_update_date, c.duration";
    public static final String FROM = " FROM gift_certificates c";
    public static final String GET_ALL_CERTIFICATE =
            String.format("%s %s;", SELECT, FROM);
    public static final String GET_CERTIFICATE_BY_NAME =
            String.format("%s %s WHERE c.name=?;", SELECT, FROM);
    public static final String INSERT_CERTIFICATE =
            String.format("INSERT INTO gift_certificates (%s) VALUES (?, ?, ?, ?, ?, ?, ?)", FIELDS);
    public static final String UPDATE_CERTIFICATE = "UPDATE gift_certificates c SET";
    public static final String DELETE_CERTIFICATE = String.format("DELETE %s  WHERE c.id=?;", FROM);
    public static final String GET_ALL_TAGS = "SELECT t.id tag_id, t.name tag_name FROM t";
    public static final String GET_TAG_BY_ID = String.format("%s WHERE t.id=?;", GET_ALL_TAGS);
    public static final String GET_BY_NAME = String.format("%s WHERE t.name =;", GET_ALL_TAGS);
    public static final String DELETE_TAG = "DELETE FROM tag WHERE tag.id=?";
    public static final String INSERT_TAG = "INSERT INTO tag (id name) VALUES (?, ?)";
    public static final String TAGS = ", t.id AS tag_id, t.name AS tag_name";
    public static final String LEFT_JOIN = " LEFT JOIN gift_certificate_tag ct ON c.id = ct.gift_certificate_id";
    public static final String LEFT_JOIN_TAG = " LEFT JOIN tag t ON ct.tag_id = t.id";

    public static final String BASE_QUERY =
            String.format("%s%s%s%s%s", SELECT, TAGS, FROM, LEFT_JOIN, LEFT_JOIN_TAG);

    public static final String SELECT_CERTIFICATES_BY_TAG_NAME =
            String.format("%s WHERE t.name iLIKE", BASE_QUERY);
    public static final String GET_CERTIFICATE_BY_ID =
            String.format("%s%s%s%s LEFT JOIN tag t ON t.id = ct.tag_id WHERE c.id = ?",
                    SELECT, TAGS, FROM, LEFT_JOIN);
}
