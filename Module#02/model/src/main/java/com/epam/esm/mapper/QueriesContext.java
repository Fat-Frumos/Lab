package com.epam.esm.mapper;

public final class QueriesContext {
    private QueriesContext() {
    }

    public static final String FIELDS = "id, name, description, price, create_date, last_update_date, duration";
    public static final String SELECT = "SELECT c.id, c.name, c.description, c.price, c.create_date, c.last_update_date, c.duration";
    public static final String FROM = " FROM gift_certificates c";
    public static final String WHERE_ID = " WHERE c.id=?";
    public static final String WHERE_NAME = " WHERE c.name=?";
    public static final String GET_ALL_CERTIFICATE = String.format("%s %s;", SELECT, FROM);
    public static final String GET_CERTIFICATE_BY_NAME = String.format("%s %s %s;", SELECT, FROM, WHERE_NAME);
    public static final String INSERT_CERTIFICATE = String.format("INSERT INTO gift_certificates (%s) VALUES (?, ?, ?, ?, ?, ?, ?)", FIELDS);
    public static final String UPDATE_CERTIFICATE = "UPDATE gift_certificates c SET";
    public static final String DELETE_CERTIFICATE = String.format("DELETE %s %s;", FROM, WHERE_ID);
    public static final String GET_ALL_TAGS = "SELECT tag.id tag_id, tag.name tag_name FROM tag";
    public static final String GET_TAG_BY_ID = String.format("%s WHERE tag.id=?;", GET_ALL_TAGS);
    public static final String GET_BY_NAME = String.format("%s WHERE tag.name=?;", GET_ALL_TAGS);
    public static final String DELETE_TAG = "DELETE FROM tag WHERE tag.id=?";
    public static final String INSERT_TAG = "INSERT INTO tag (id name) VALUES (?, ?)";
    public static final String TAGS = ", t.id AS tag_id, t.name AS tag_name";
    public static final String LEFT_JOIN = " LEFT JOIN gift_certificate_tag ct ON c.id = ct.gift_certificate_id";
    public static final String LEFT_JOIN_TAG = " LEFT JOIN tag t ON ct.tag_id = t.id";
    public static final String WHERE = " WHERE 1=1";
    public static final String BASE_QUERY = SELECT + TAGS + FROM + LEFT_JOIN + LEFT_JOIN_TAG;

    public static final String SELECT_CERTIFICATES_BY_TAG_NAME = BASE_QUERY + WHERE;
    public static final String GET_CERTIFICATE_BY_ID =
            SELECT + TAGS + FROM + LEFT_JOIN +
                    " LEFT JOIN tag t ON t.id = ct.tag_id" +
                    " WHERE c.id = ?";
    public static final String GET_ALL_WITH_TAG_ID =
            SELECT + TAGS + FROM +
                    " LEFT JOIN gift_certificate_tag ct" +
                    " ON ct.gift_certificate_id = c.id" +
                    " LEFT JOIN tag t ON t.id = ct.tag_id;";

    public static final String GET_ALL_BY_TAG_NAME_ORDER =
            BASE_QUERY +
            " WHERE t.name LIKE '%Birthday%'" +
            " ORDER BY c.name DESC, c.create_date DESC";

}
