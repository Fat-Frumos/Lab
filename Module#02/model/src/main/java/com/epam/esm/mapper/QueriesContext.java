package com.epam.esm.mapper;

/**
 * A class containing SQL queries used by the Gift Certificate service.
 */
public final class QueriesContext {

    private QueriesContext() {
    }

    /**
     * The SELECT statement used to retrieve all Gift Certificates from the database.
     */
    public static final String SELECT = "SELECT c.id, c.name, c.description, c.price, c.create_date, c.last_update_date, c.duration";

    /**
     * The FROM clause used in all Gift Certificate queries.
     */
    public static final String FROM = " FROM gift_certificates c";

    /**
     * The SQL query used to retrieve all Gift Certificates.
     */
    public static final String GET_ALL_CERTIFICATE = String.format("%s %s;", SELECT, FROM);

    /**
     * The SQL query used to retrieve a Gift Certificate by name.
     */
    public static final String GET_CERTIFICATE_BY_NAME = String.format("%s %s WHERE c.name=", SELECT, FROM);

    /**
     * The SQL query used to insert a new Gift Certificate into the database.
     */
    public static final String INSERT_CERTIFICATE =
            "INSERT INTO gift_certificates (id, name, description, price, duration, create_date, last_update_date) VALUES (?, ?, ?, ?, ?, ?, ?)";

    /**
     * The SQL query used to update a Gift Certificate in the database.
     */
    public static final String UPDATE_CERTIFICATE = "UPDATE gift_certificates c SET";

    /**
     * The SQL query used to delete a Gift Certificate from the database.
     */
    public static final String DELETE_CERTIFICATE = String.format("DELETE %s WHERE c.id=?;", FROM);

    /**
     * The SQL query used to delete a reference to a Gift Certificate from the gift_certificate_tag table.
     */
    public static final String DELETE_REF = "DELETE FROM gift_certificate_tag WHERE gift_certificate_id = ?";

    /**
     * The SQL query used to retrieve all Tags from the database.
     */
    public static final String GET_ALL_TAGS = "SELECT t.id tag_id, t.name tag_name FROM tag as t";

    /**
     * The SQL query used to retrieve a Tag by ID.
     */
    public static final String GET_TAG_BY_ID = String.format("%s WHERE t.id=?;", GET_ALL_TAGS);

    /**
     * The SQL query used to retrieve a Gift Certificate by Tag name.
     */
    public static final String GET_BY_TAG_NAME = String.format("%s WHERE t.name =", GET_ALL_TAGS);

    /**
     * The SQL query used to delete a Tag from the database.
     */
    public static final String DELETE_TAG = "DELETE FROM tag WHERE tag.id=?";

    /**
     * The SQL query used to insert a new Tag into the database.
     */
    public static final String INSERT_TAG = "INSERT INTO tag (id, name) VALUES (?, ?)";

    /**
     * The SQL query used to delete a reference to a Tag from the gift_certificate_tag table.
     */
    public static final String DELETE_TAG_REF = "DELETE FROM gift_certificate_tag WHERE tag_id = ?";

    /**
     * The SELECT clause used to retrieve Gift Certificates and their associated Tags from the database.
     */
    public static final String TAGS = ", t.id AS tag_id, t.name AS tag_name";

    /**
     * The LEFT JOIN clause used to join the gift_certificates table with the gift_certificate_tag table.
     */
    public static final String LEFT_JOIN = " LEFT JOIN gift_certificate_tag ct ON c.id = ct.gift_certificate_id";

    /**
     * The SQL query for a left join on the 'tag' table.
     */
    public static final String LEFT_JOIN_TAG = " LEFT JOIN tag t ON ct.tag_id = t.id";

    /**
     * The base SQL query for selecting certificates with left joins.
     */
    public static final String BASE_QUERY =
            String.format("%s%s%s%s%s", SELECT, TAGS, FROM, LEFT_JOIN, LEFT_JOIN_TAG);

    /**
     * The SQL query for selecting certificates by tag name.
     */
    public static final String SELECT_CERTIFICATES_BY_TAG_NAME =
            String.format("%s WHERE t.name iLIKE", BASE_QUERY);
    /**
     * The SQL query for selecting a certificate by ID.
     */
    public static final String GET_CERTIFICATE_BY_ID =
            String.format("%s%s%s%s LEFT JOIN tag t ON t.id = ct.tag_id WHERE c.id = ?",
                    SELECT, TAGS, FROM, LEFT_JOIN);
}
