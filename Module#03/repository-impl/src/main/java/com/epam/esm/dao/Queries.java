package com.epam.esm.dao;

public final class Queries {
    private Queries() {
    }
    public static final String SELECT_ALL = "SELECT c FROM Certificate c";
    public static final String SELECT_BY_NAME = SELECT_ALL + " WHERE c.name = :name";
    public static final String SELECT_CERTIFICATES_BY_USER_ID = "SELECT c FROM Certificate c JOIN c.orders o JOIN o.user u WHERE u.id = :id";
    public static final String SELECT_ALL_BY_IDS = "SELECT c FROM Certificate c WHERE c.id IN :ids";
    public static final String SELECT_TAGS_BY_NAME  = "SELECT t FROM Tag t WHERE t.name = :name";
    public static final String SELECT_TAGS_BY_ID = "SELECT t FROM Tag t JOIN t.certificates c WHERE c.id = :id";
    public static final String SELECT_ORDER_BY_NAME = "SELECT o FROM Order o WHERE o.user.username = :username";
    public static final String SELECT_USER_BY_NAME = "SELECT u FROM User u WHERE u.username = :name";
}
