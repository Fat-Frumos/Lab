package com.epam.esm.dao;

/**
 * Contains constant strings for SQL queries.
 */
public final class Queries {
    private Queries() {
    }

    public static final String CERTIFICATES = "certificates";
    public static final String TAGS = "tags";
    /**
     * Constant for the fetch graph hint used in entity manager queries.
     */
    public static final String FETCH_GRAPH = "jakarta.persistence.fetchgraph";
    /**
     * SQL query to select all certificates.
     */
    public static final String SELECT_ALL = "SELECT c FROM Certificate c";

    /**
     * SQL query to select a certificate by name.
     */
    public static final String SELECT_BY_NAME = SELECT_ALL + " WHERE c.name = :name";

    /**
     * SQL query to select certificates by user ID.
     */
    public static final String SELECT_CERTIFICATES_BY_USER_ID = "SELECT c FROM Certificate c JOIN c.orders o JOIN o.user u WHERE u.id = :id";

    /**
     * SQL query to select certificates by a list of IDs.
     */
    public static final String SELECT_ALL_BY_IDS = "SELECT c FROM Certificate c WHERE c.id IN :ids";
    /**
     * SQL query to select all certificates.
     */
    public static final String SELECT_ALL_CERTIFICATES =
            "SELECT c.id, c.create_date, c.description, c.duration, c.last_update_date, c.name, c.price "
                    + "FROM gift_certificates c JOIN gift_certificate_tag ct ON c.id = ct.gift_certificate_id "
                    + "JOIN tag t ON ct.tag_id = t.id ORDER BY c.id ASC OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY";

    /**
     * SQL query to select certificates by order ID.
     */
    public static final String SELECT_CERTIFICATES_BY_ORDER_ID =
            "SELECT DISTINCT c "
                    + "FROM Certificate c "
                    + "JOIN FETCH c.orders o "
                    + "JOIN FETCH o.user "
                    + "WHERE o.id = :orderId";

    /**
     * SQL query to select tags by name.
     */
    public static final String SELECT_TAGS_BY_NAME = "SELECT t FROM Tag t WHERE t.name = :name";

    /**
     * SQL query to select tags by certificate ID.
     */
    public static final String SELECT_TAGS_BY_ID = "SELECT t FROM Tag t JOIN t.certificates c WHERE c.id = :id";

    /**
     * SQL query to select an order by username.
     */
    public static final String SELECT_ORDER_BY_NAME = "SELECT o FROM Order o WHERE o.user.username = :username";
    public static final String SELECT_ORDER_BY_ID = "SELECT DISTINCT o FROM Order o "
            + "LEFT JOIN FETCH o.certificates c "
            + "LEFT JOIN FETCH c.tags "
            + "LEFT JOIN FETCH o.user "
            + "WHERE o.id = :id";

    /**
     * SQL query to select a user by name.
     */
    public static final String SELECT_USER_BY_NAME = "SELECT u FROM User u WHERE u.username = :name";

    public static final String SELECT_ALL_ORDERS = "SELECT DISTINCT o FROM Order o JOIN FETCH o.certificates fetch first ? rows only";

    public static final String SELECT_TAG_BY_MANE = "SELECT t FROM Tag t WHERE t.name = :name";;

}
