package com.epam.esm.mapper;

public final class QueriesContext {
    private QueriesContext() {
    }

    public static final String FIELDS_CERTIFICATE = "id, name, description, price, create_date, last_update_date, duration";
    private static final String FROM = "FROM gift_certificates";
    private static final String WHERE_ID = "WHERE id=?";
    private static final String WHERE_NAME = "WHERE name=?";
    public static final String GET_ALL_CERTIFICATE = String.format("SELECT %s %s;", FIELDS_CERTIFICATE, FROM);
    public static final String GET_CERTIFICATE_BY_ID = String.format("SELECT %s %s %s;", FIELDS_CERTIFICATE, FROM, WHERE_ID);
    public static final String GET_CERTIFICATE_BY_NAME = String.format("SELECT %s %s %s;", FIELDS_CERTIFICATE, FROM, WHERE_NAME);
    public static final String INSERT_CERTIFICATE = String.format("INSERT INTO gift_certificates (%s) VALUES (?, ?, ?, ?, ?, ?, ?)", FIELDS_CERTIFICATE);
    public static final String UPDATE_CERTIFICATE = "UPDATE gift_certificates SET ";
    public static final String DELETE_CERTIFICATE = String.format("DELETE %s %s;", FROM, WHERE_ID);
    public static final String GET_ALL_TAGS = "SELECT tag.id t_id, tag.name t_name FROM tag";
    public static final String GET_TAG_BY_ID = String.format("%s WHERE tag.id=?;", GET_ALL_TAGS);
    public static final String GET_BY_NAME = String.format("%s WHERE tag.name=?;", GET_ALL_TAGS);
    public static final String DELETE_TAG = "DELETE FROM tag WHERE id=?";
    public static final String INSERT_TAG = "INSERT INTO tag (id name) VALUES (?, ?)";
}