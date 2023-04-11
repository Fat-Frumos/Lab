package com.epam.esm;

import com.epam.esm.criteria.Criteria;
import com.epam.esm.criteria.QueryBuilder;
import com.epam.esm.criteria.SortField;
import com.epam.esm.criteria.SortOrder;
import com.epam.esm.entity.Certificate;
import com.epam.esm.mapper.CertificateListExtractor;
import com.epam.esm.mapper.CertificateRowMapper;
import com.epam.esm.mapper.TagRowMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static com.epam.esm.mapper.QueriesContext.BASE_QUERY;
import static com.epam.esm.mapper.QueriesContext.FROM;
import static com.epam.esm.mapper.QueriesContext.LEFT_JOIN_TAG;
import static com.epam.esm.mapper.QueriesContext.SELECT;
import static com.epam.esm.mapper.QueriesContext.SELECT_CERTIFICATES_BY_TAG_NAME;
import static com.epam.esm.mapper.QueriesContext.TAGS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@ActiveProfiles("test")
@TestPropertySource("classpath:application-test.properties")
class CriteriaTest {

    private JdbcTemplate jdbcTemplate;
    public final TagRowMapper tagRowMapper = new TagRowMapper();
    private final CertificateRowMapper rowMapper = new CertificateRowMapper();
    private final CertificateListExtractor listExtractor = new CertificateListExtractor(tagRowMapper, rowMapper);
    public static final String GET_ALL_LIKE =
            String.format("%s WHERE (c.name iLIKE '%%gift%%' OR c.description iLIKE '%%certificate%%');", BASE_QUERY);
    public static final String GET_ALL_WITH_TAGS =
            String.format("%s%s%s LEFT JOIN gift_certificate_tag ct ON c.id = ct.gift_certificate_id%s;",
                    SELECT, TAGS, FROM, LEFT_JOIN_TAG);

    @BeforeEach
    void setUp() {
        EmbeddedDatabase dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("classpath:db/schema.sql")
                .addScript("classpath:db/data.sql")
                .ignoreFailedDrops(true)
                .setName("db")
                .build();
//        dataSource.setSchema("classpath:db/data.sql");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    @DisplayName("Test finding all certificates by default criteria")
    void testFindAllWithEmptyCriteria() {
        Criteria emptyCriteria = Criteria.builder().build();
        String queryGetAllWithTags = QueryBuilder.builder().criteria(emptyCriteria).build();
        System.out.println(queryGetAllWithTags);
        assertEquals(GET_ALL_WITH_TAGS, queryGetAllWithTags);
        List<Certificate> certificateList = jdbcTemplate.query(queryGetAllWithTags, listExtractor);
        log.info(String.valueOf(Objects.requireNonNull(certificateList).size()));
    }

    @ParameterizedTest(name = "{0}")
    @CsvSource({"Birthday", "Easter", "Spring"})
    @DisplayName("Test finding all certificates by tag name")
    void testFindAllWithCriteriaTagNames(String tagName) {
        Criteria criteria = Criteria.builder().tagName(tagName).build();
        String queryByTagName = QueryBuilder.builder().criteria(criteria).build();
        System.out.println(queryByTagName);
        String[] split = queryByTagName.split(" ");
        String sql = String.format("%s '%%%s%%';", SELECT_CERTIFICATES_BY_TAG_NAME, tagName);
        String[] splits = SELECT_CERTIFICATES_BY_TAG_NAME.split(" ");
        IntStream.range(0, splits.length).forEach(i -> assertEquals(split[i], splits[i]));

        List<Certificate> listTags = jdbcTemplate.query(queryByTagName, listExtractor);
        List<Certificate> list = jdbcTemplate.query(sql, listExtractor);
        assertEquals(Objects.requireNonNull(list).size(), Objects.requireNonNull(listTags).size());
        IntStream.range(0, listTags.size()).forEach(i -> assertEquals(listTags.get(i), list.get(i)));
    }

    @Test
    @DisplayName("Test search all certificates by part of name or description")
    void testFindAllWithDiffCriteria() {
        Criteria criteria = Criteria.builder().name("gift").description("certificate").build();
        String searchBy = QueryBuilder.builder().searchBy(criteria).build();
        List<Certificate> list = jdbcTemplate.query(searchBy, listExtractor);
        List<Certificate> listLike = jdbcTemplate.query(GET_ALL_LIKE, listExtractor);
        assertEquals(Objects.requireNonNull(listLike).size(), Objects.requireNonNull(list).size());
        IntStream.range(0, listLike.size()).forEach(i -> assertEquals(list.get(i), listLike.get(i)));
    }

    @ParameterizedTest(name = "criteria: name={0}, description={1}, tagName={2}, sortOrder={3}, sortField={4}, date={5}")
    @CsvSource({
            ",Certificate,,DESC,,,DESC,NAME,",
            ",,Gift,,,,UNSORTED,DATE,Gift",
            "Spring,,,,,,UNSORTED,DATE,",
            ",Certificate,,ASC,,,ASC,DATE,",
            ",Certificate,,UNSORTED,,,UNSORTED,DATE,",
    })
    void testCriteria_withRequiredFields(String name, String description, String tagName, String sortOrder, String sortField, Instant date, String expectedOrder, String expectedField, String expectedTagName) {

        SortOrder order = sortOrder != null ? SortOrder.valueOf(sortOrder) : null;
        SortField field = sortField != null ? SortField.valueOf(sortField) : null;
        if (date == null) {
            date = Instant.now();
        }
        if (order == null) {
            order = SortOrder.UNSORTED;
        }

        Criteria criteria = new Criteria(order, field, name, description, tagName, date);
        String searchBy = QueryBuilder.builder().searchBy(criteria).build();
        System.out.println(searchBy);
        Objects.requireNonNull(jdbcTemplate.query(searchBy, listExtractor))
                .forEach(Assertions::assertNotNull);
        assertNotNull(criteria);
        assertEquals(expectedOrder, criteria.getSortOrder().toString());
        if (field != null) {
            assertEquals(expectedField, criteria.getSortField().toString());
        }
        assertEquals(date, criteria.getDate());
        assertEquals(expectedTagName, criteria.getTagName());
    }

    @ParameterizedTest
    @CsvSource({
            "Birthday,,Day,35",
            ",Gift,Day,40",
            "Birthday,Gift,,3",
            "Birthday,Gift,Day,50",
            "Birthday,,,5",
            ",Gift,,36",
            ",,Day,65",
            ",,,84"
    })
    @DisplayName("Test search all certificates by part of name or description")
    void testSearchCertificates(String tagName, String name, String description, int size) {
        Criteria criteria = Criteria.builder()
                .tagName(tagName)
                .name(name)
                .description(description)
                .build();

        List<Certificate> certificates = Objects.requireNonNull(jdbcTemplate.query(QueryBuilder.builder().searchBy(criteria).build(), listExtractor));

        assertEquals(size, certificates.size());

        certificates.forEach(certificate -> assertTrue(matches(criteria, certificate)));
    }


    private boolean matches(Criteria criteria, Certificate certificate) {

        if (criteria.getName() != null && certificate.getName().toLowerCase().contains(criteria.getName().toLowerCase())) {
            return true;
        }
        if (criteria.getDescription() != null && certificate.getDescription().toLowerCase().contains(criteria.getDescription().toLowerCase())) {
            return true;
        }
        if (criteria.getTagName() != null && certificate.getTags().stream()
                .anyMatch(tag -> tag.getName().toLowerCase().contains(criteria.getTagName().toLowerCase()))) {
            return true;
        }
        return criteria.getName() == null && criteria.getDescription() == null && criteria.getTagName() == null;
    }
}
