package com.epam.esm.criteria;

public final class CertificateQueries {
    private CertificateQueries() {
    }
    public static final String SELECT_ALL = "SELECT c FROM Certificate c";
    public static final String SELECT_TAGS_BY_ID = "SELECT t FROM Tag t JOIN t.certificates c WHERE c.id = :id";
    public static final String SELECT_ALL_WITH_TAGS = SELECT_ALL
            + " LEFT JOIN FETCH c.tags "
            + " ORDER BY c.createDate DESC";
    public static final String SELECT_BY_NAME = SELECT_ALL
            + " WHERE c.name = :name";
    public static final String SELECT_BY_IDS = SELECT_ALL + " WHERE c.id IN :ids";

    public static final String SELECT_USER_CERTIFICATES = SELECT_ALL + " JOIN c.orders o JOIN o.user u WHERE u.id = :id";
    public static final String SELECT_ALL_WITH_TAGS_ORDERS = "SELECT DISTINCT c FROM Certificate c LEFT JOIN FETCH c.tags LEFT JOIN FETCH c.orders o LEFT JOIN FETCH o.user ORDER BY c.createDate DESC";
    public static final String SELECT_TAG_NAMES = "SELECT DISTINCT c FROM Certificate c JOIN c.tags t WHERE t.name IN :tagNames";
}
