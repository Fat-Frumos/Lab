package com.epam.esm.dao;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.exception.CertificateNotFoundException;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.Subgraph;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.hibernate.query.NativeQuery;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.epam.esm.dao.Queries.FETCH_GRAPH;
import static com.epam.esm.dao.Queries.ID;
import static com.epam.esm.dao.Queries.ORDERS;
import static com.epam.esm.dao.Queries.SELECT_ALL_CERTIFICATES;
import static com.epam.esm.dao.Queries.SELECT_CERTIFICATES_BY_ORDER_ID;
import static com.epam.esm.dao.Queries.TAGS;
import static com.epam.esm.dao.Queries.USER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class CertificateDaoTest {
    @Mock
    private EntityManagerFactory factory;
    @Mock
    private EntityTransaction transaction;
    @Mock
    private EntityManager entityManager;
    @Mock
    private TypedQuery<Tag> tagTypedQuery;
    @Mock
    private CriteriaBuilder criteriaBuilder;
    @Mock
    private CriteriaQuery<Certificate> criteriaQuery;
    @Mock
    private TypedQuery<Certificate> typedQuery;
    @Mock
    NativeQuery<Certificate> nativeQuery;
    @Mock
    private Join<Certificate, Tag> tagJoin;
    private CertificateDao certificateDao;
    private final Pageable pageable = PageRequest.of(0, 25, Sort.by("name").ascending());
    @Mock
    private Root<Certificate> root;
    @Mock
    private Path<Object> path;
    @Mock
    private EntityGraph<Certificate> graph;
    @Mock
    private TypedQuery<Tag> tagDtoTypedQuery;
    @Mock
    private Subgraph<Object> subgraph;
    private final Long id = 1L;
    private final Certificate certificate = Certificate.builder().id(id).build();
    List<Tag> expectedTags = new ArrayList<>();

    @BeforeEach
    void setUp() {
        certificateDao = new CertificateDaoImpl(factory);

        expectedTags.add(Tag.builder().id(id).name("Tag").build());
        expectedTags.add(Tag.builder().id(id + 2).name("namespace").build());

        when(factory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.createEntityGraph(Certificate.class)).thenReturn(graph);
        when(entityManager.createQuery(anyString(), eq(Tag.class))).thenReturn(tagTypedQuery);
        when(tagTypedQuery.setParameter(anyString(), any())).thenReturn(tagTypedQuery);
    }

//    @Test
//    void testFindAllByOrderId() {
//
//        when(factory.createEntityManager()).thenReturn(entityManager);
//        when(entityManager.createEntityGraph(Certificate.class)).thenReturn(graph);
//        when(graph.addSubgraph(ORDERS)).thenReturn(subgraph);
//        when(entityManager.createQuery(SELECT_CERTIFICATES_BY_ORDER_ID, Certificate.class)).thenReturn(typedQuery);
//        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
//        when(typedQuery.setHint(anyString(), any())).thenReturn(typedQuery);
//        List<Certificate> expectedCertificates = Arrays.asList(certificate, certificate);
//        when(typedQuery.getResultList()).thenReturn(expectedCertificates);
//
//        Set<Certificate> actualCertificates = certificateDao.findAllByOrderId(id);
//
//        assertEquals(new HashSet<>(expectedCertificates), actualCertificates);
//        verify(entityManager).createQuery(SELECT_CERTIFICATES_BY_ORDER_ID, Certificate.class);
//        verify(graph).addAttributeNodes(TAGS);
//        verify(subgraph).addAttributeNodes(USER);
//        verify(typedQuery).setParameter("orderId", id);
//    }

//    @Test
//    @DisplayName("Test Get All using NativeQuery")
//    void testGetAll() {
//        when(factory.createEntityManager()).thenReturn(entityManager);
//        when(entityManager.createNativeQuery(SELECT_ALL_CERTIFICATES)).thenReturn(nativeQuery);
//        when(nativeQuery.setParameter(anyString(), anyInt())).thenReturn(nativeQuery);
//        when(nativeQuery.unwrap(NativeQuery.class)).thenReturn(nativeQuery);
//        when(nativeQuery.addEntity(Certificate.class)).thenReturn(nativeQuery);
//        List<Certificate> expectedCertificates = Arrays.asList(certificate, certificate);
//        when(nativeQuery.getResultList()).thenReturn(expectedCertificates);
//
//        List<Certificate> actualCertificates = certificateDao.getAll(pageable);
//
//        assertEquals(expectedCertificates, actualCertificates);
//        verify(entityManager).createNativeQuery(SELECT_ALL_CERTIFICATES);
//        verify(nativeQuery).setParameter("offset", pageable.getPageNumber() * pageable.getPageSize());
//        verify(nativeQuery).setParameter("limit", pageable.getPageSize());
//    }

//    @Test
//    @DisplayName("Test Get All using graph")
//    void testGetAllBy() {
//        List<Certificate> expectedCertificates = Arrays.asList(certificate, certificate);
//        when(factory.createEntityManager()).thenReturn(entityManager);
//        when(entityManager.createEntityGraph(Certificate.class)).thenReturn(graph);
//        when(entityManager.createQuery(anyString(), eq(Certificate.class))).thenReturn(typedQuery);
//        when(typedQuery.setHint(anyString(), any())).thenReturn(typedQuery);
//        when(typedQuery.setFirstResult(anyInt())).thenReturn(typedQuery);
//        when(typedQuery.setMaxResults(anyInt())).thenReturn(typedQuery);
//        when(typedQuery.getResultList()).thenReturn(expectedCertificates);
//
//        List<Certificate> actualCertificates = certificateDao.getAllBy(pageable);
//        assertEquals(expectedCertificates, actualCertificates);
//        verify(entityManager).createQuery(anyString(), eq(Certificate.class));
//        verify(graph).addAttributeNodes("tags");
//        verify(typedQuery).setHint(FETCH_GRAPH, graph);
//        verify(typedQuery).setFirstResult((int) pageable.getOffset());
//        verify(typedQuery).setMaxResults(pageable.getPageSize());
//    }

    @Test
    @DisplayName("Test find Certificate by ID")
    void testFindById() {
        Long id = 1L;
        Certificate expectedCertificate = Certificate.builder()
                .id(id).name("certificate1")
                .build();
        when(factory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.find(eq(Certificate.class), eq(id), anyMap())).thenReturn(expectedCertificate);
        Certificate actualCertificate = certificateDao.findById(id);
        assertEquals(expectedCertificate, actualCertificate);
        verify(entityManager).close();
    }

//    @DisplayName("Test update Certificate")
//    @ParameterizedTest
//    @CsvSource({
//            "1, Winter, Season 1, 10.0, 30",
//            "2, Summer, Season 2, 20.0, 45",
//            "3, Spring, Season 3, 30.0, 60",
//            "4, Autumn, Season 4, 40.0, 75"})
//    void testUpdateCertificate(Long id, String name,
//                               String description,
//                               BigDecimal price, Integer duration) {
//        Certificate certificate = Certificate
//                .builder().id(id).name(name)
//                .description(description).price(price)
//                .duration(duration).build();
//        Certificate existed = Certificate.builder()
//                .id(id).name("oldName")
//                .description("oldDescription").price(BigDecimal.ONE)
//                .duration(10).build();
//
//        when(factory.createEntityManager()).thenReturn(entityManager);
//        when(entityManager.getTransaction()).thenReturn(transaction);
//        when(entityManager.getReference(Certificate.class, id)).thenReturn(existed);
//
//        Certificate actualCertificate = certificateDao.update(certificate);
//
//        assertEquals(certificate.getName(), actualCertificate.getName());
//        assertEquals(certificate.getDescription(), actualCertificate.getDescription());
//        assertEquals(certificate.getPrice(), actualCertificate.getPrice());
//        assertEquals(certificate.getDuration(), actualCertificate.getDuration());
//
//        verify(entityManager).getTransaction();
//        verify(transaction).begin();
//        verify(entityManager).getReference(Certificate.class, id);
//        verify(transaction).commit();
//        verify(entityManager).close();
//    }

    @DisplayName("Test find certificate by ID")
    @ParameterizedTest(name = "Test #{index} - ID: {0}, Name: {1}, Description: {2}")
    @CsvSource({
            "1, Winter, Season 1, 30",
            "2, Summer, Season 2, 45",
            "3, Spring, Season 3, 60",
            "4, Autumn, Season 4, 75"})
    void testFindById(Long id, String name, String description, Long id2) {
        List<Tag> expectedTags = new ArrayList<>();
        expectedTags.add(Tag.builder().id(id).name(name).build());
        expectedTags.add(Tag.builder().id(id2).name(description).build());
        when(entityManager.createEntityGraph(Certificate.class)).thenReturn(graph);
        when(entityManager.createQuery(anyString(), eq(Tag.class))).thenReturn(tagTypedQuery);
        when(tagTypedQuery.setParameter(anyString(), any())).thenReturn(tagTypedQuery);
        when(tagTypedQuery.getResultList()).thenReturn(expectedTags);

        List<Tag> actualTags = certificateDao.findTagsByCertificateId(id);

        assertEquals(expectedTags, actualTags);
        verify(entityManager).createQuery(anyString(), eq(Tag.class));
        verify(tagTypedQuery).setParameter(anyString(), any());
        verify(tagTypedQuery).getResultList();
    }

    @DisplayName("Save certificate")
    @ParameterizedTest(name = "Test #{index} - ID: {0}, Name: {1}, Description: {2}")
    @CsvSource({
            "1, Winter, Season 1, 10.0, 30",
            "2, Summer, Season 2, 20.0, 45",
            "3, Spring, Season 3, 30.0, 60",
            "4, Autumn, Season 4, 40.0, 75"})
    void testSave(Long id, String name, String description, BigDecimal price, int duration) {

        Certificate certificate = Certificate.builder()
                .id(id).name(name)
                .description(description).price(price)
                .duration(duration).build();

        Tag tag = Tag.builder().name("Seasonal").build();
        Set<Tag> tags = new HashSet<>();
        tags.add(tag);
        certificate.setTags(tags);
        String sql = "SELECT t FROM Tag t WHERE t.name = :name";
        when(factory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        when(entityManager.createQuery(sql, Tag.class)).thenReturn(tagTypedQuery);
        when(tagTypedQuery.setParameter("name", tag.getName())).thenReturn(tagTypedQuery);
        when(tagTypedQuery.getResultList()).thenReturn(Collections.singletonList(tag));

        Certificate actualCertificate = certificateDao.save(certificate);

        assertEquals(certificate, actualCertificate);
        verify(entityManager).getTransaction();
        verify(transaction).begin();
        verify(entityManager).createQuery(sql, Tag.class);
        verify(tagTypedQuery).setParameter("name", tag.getName());
        verify(tagTypedQuery).getResultList();
        verify(entityManager).persist(certificate);
        verify(transaction).commit();
    }

//    @DisplayName("Test Find By Tag Names With Certificates")
//    @ParameterizedTest(name = "Test #{index} - ID: {0}, Name: {1}, Description: {2}, Price: {3}, Duration: {4}")
//    @CsvSource({
//            "1, Winter, Season 1, 10.0, 30",
//            "2, Summer, Season 2, 20.0, 45",
//            "3, Spring, Season 3, 30.0, 60",
//            "4, Autumn, Season 4, 40.0, 75"})
//    void testFindByTagNamesWithCertificates(long id, String name, String description, BigDecimal price, int duration) {
//        List<String> tagNames = Arrays.asList("Season", "Year");
//        Predicate[] predicates = tagNames.stream()
//                .map(tagName -> criteriaBuilder.equal(tagJoin.get("name"), tagName))
//                .toArray(Predicate[]::new);
//        Predicate finalPredicate = criteriaBuilder.and(predicates);
//        List<Certificate> expectedCertificates = new ArrayList<>();
//        Certificate certificate = Certificate.builder()
//                .id(id).name(name)
//                .description(description).price(price)
//                .duration(duration).build();
//        expectedCertificates.add(certificate);
//
//        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
//        when(criteriaBuilder.createQuery(Certificate.class)).thenReturn(criteriaQuery);
//        when(criteriaQuery.from(Certificate.class)).thenReturn(root);
//        doReturn(tagJoin).when(root).join("tags", JoinType.INNER);
//        when(tagJoin.get("name")).thenReturn(path);
//        when(criteriaBuilder.and(predicates)).thenReturn(finalPredicate);
//        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);
//        when(criteriaQuery.where(finalPredicate)).thenReturn(criteriaQuery);
//        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
//        when(typedQuery.getResultList()).thenReturn(expectedCertificates);
//
//        List<Certificate> actualCertificates = certificateDao.findByTagNames(tagNames);
//
//        assertEquals(expectedCertificates, actualCertificates);
//        verify(factory).createEntityManager();
//        verify(entityManager).getCriteriaBuilder();
//        verify(criteriaBuilder).createQuery(Certificate.class);
//        verify(criteriaQuery).from(Certificate.class);
//        verify(criteriaQuery).where(finalPredicate);
//        verify(entityManager).createQuery(criteriaQuery);
//        verify(typedQuery).getResultList();
//    }

//    @DisplayName("Get certificates by user ID")
//    @ParameterizedTest(name = "Test #{index} - ID: {0}, Name: {1}, Description: {2}, Price: {3}, Duration: {4}")
//    @CsvSource({
//            "1, Winter, Season 1, 10.0, 30",
//            "2, Summer, Season 2, 20.0, 45",
//            "3, Spring, Season 3, 30.0, 60",
//            "4, Autumn, Season 4, 40.0, 75"})
//    void getCertificatesByUserIdTest(Long id, String name, String description, BigDecimal price, int duration) {
//        List<Certificate> expectedCertificates = Collections.singletonList(
//                Certificate.builder()
//                        .id(id)
//                        .name(name)
//                        .description(description)
//                        .price(price)
//                        .duration(duration)
//                        .build()
//        );
//        when(factory.createEntityManager()).thenReturn(entityManager);
//        when(entityManager.createQuery(SELECT_CERTIFICATES_BY_USER_ID, Certificate.class)).thenReturn(typedQuery);
//        when(typedQuery.setParameter("id", id)).thenReturn(typedQuery);
//        when(typedQuery.getResultList()).thenReturn(expectedCertificates);
//
//        List<Certificate> actualCertificates = certificateDao.getCertificatesByUserId(id);
//        assertEquals(expectedCertificates, actualCertificates);
//
//        verify(factory).createEntityManager();
//        verify(typedQuery).getResultList();
//    }

    @DisplayName("Get certificates by user ID")
    @Test
    void findAllByIdsTest() {
        Set<Long> certificateIds = new HashSet<>(Arrays.asList(1L, 2L, 3L, 4L));

        when(entityManager.createQuery(anyString(), eq(Certificate.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), anySet())).thenReturn(typedQuery);
        when(typedQuery.setHint(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.emptyList());

        certificateDao.findAllByIds(certificateIds);

        verify(factory).createEntityManager();
        verify(entityManager).createEntityGraph(Certificate.class);
        verify(entityManager).createQuery(anyString(), eq(Certificate.class));
        verify(typedQuery).setParameter(anyString(), anySet());
        verify(typedQuery).setHint(anyString(), any());
        verify(typedQuery).getResultList();
    }

//    @Test
//    @DisplayName("Test update Certificate not found and verify rollback transaction")
//    void testUpdateCertificateRollback() {
//        when(factory.createEntityManager()).thenReturn(entityManager);
//        when(entityManager.getTransaction()).thenReturn(transaction);
//        when(entityManager.find(Certificate.class, id)).thenReturn(null);
//        when(transaction.isActive()).thenReturn(true);
//        assertThrows(PersistenceException.class, () -> certificateDao.update(certificate));
//        verify(entityManager).getTransaction();
//        verify(transaction).begin();
//        verify(entityManager).find(Certificate.class, id);
//        verify(transaction).rollback();
//        verify(entityManager).close();
//        verifyNoMoreInteractions(entityManager, typedQuery, graph);
//    }


    @Test
    @DisplayName("Test save Entity not found")
    void testSaveCertificateNotFound() {
        when(factory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(transaction);
        doThrow(new RuntimeException("Error")).when(transaction).commit();
        when(transaction.isActive()).thenReturn(true);
        assertThrows(PersistenceException.class, () -> certificateDao.save(certificate));
        verify(entityManager).getTransaction();
        verify(transaction).begin();
        verify(transaction).rollback();
    }

//    @Test
//    @DisplayName("Test delete Tag verify commit transaction")
//    void testDelete() {
//        Certificate entity = mock(Certificate.class);
//        when(entityManager.getTransaction()).thenReturn(transaction);
//        when(entityManager.getReference(Certificate.class, id)).thenReturn(entity);
//        when(factory.createEntityManager()).thenReturn(entityManager);
//
//        certificateDao.delete(id);
//
//        verify(entityManager).getTransaction();
//        verify(transaction).begin();
//        verify(entityManager).getReference(Certificate.class, id);
//        verify(entityManager).remove(entity);
//        verify(transaction).commit();
//    }

    @ParameterizedTest(name = "Test #{index} - Name: {0}")
    @CsvSource({
            "Winter",
            "Summer",
            "Spring",
            "Autumn"})
    void testGetByNameCertificateNotFound(String name) {

        when(factory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(anyString(), eq(Certificate.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.emptyList());
        Optional<Certificate> result = certificateDao.getByName(name);
        assertFalse(result.isPresent());
        verify(entityManager).createQuery(anyString(), eq(Certificate.class));
        verify(typedQuery).setParameter(anyString(), any());
        verify(typedQuery).getResultList();
    }

    @DisplayName("Test delete Entity not found and verify rollback transaction")
    @ParameterizedTest(name = "Test #{index} - ID: {0}, Name: {1}, Description: {2}")
    @CsvSource({
            "1, Winter, Season 1, 10.0, 30",
            "2, Summer, Season 2, 20.0, 45",
            "3, Spring, Season 3, 30.0, 60",
            "4, Autumn, Season 4, 40.0, 75"})
    void testGetByNameCertificateFound(Long id, String name, String description, BigDecimal price, int duration) {

        Certificate certificate = Certificate.builder()
                .id(id).name(name)
                .description(description).price(price)
                .duration(duration).build();

        when(factory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.createQuery(anyString(), eq(Certificate.class))).thenReturn(typedQuery);
        when(typedQuery.setParameter(anyString(), any())).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(Collections.singletonList(certificate));

        Optional<Certificate> result = certificateDao.getByName(name);

        assertTrue(result.isPresent());
        assertEquals(certificate, result.get());

        verify(entityManager).createQuery(anyString(), eq(Certificate.class));
        verify(typedQuery).setParameter(anyString(), any());
        verify(typedQuery).getResultList();
    }

//    @Test
//    @DisplayName("Test delete Tag not found and verify rollback transaction and Throws Exception")
//    void testDeleteThrowsException() {
//        when(factory.createEntityManager()).thenReturn(entityManager);
//        when(entityManager.getTransaction()).thenReturn(transaction);
//        when(transaction.isActive()).thenReturn(true);
//        when(entityManager.find(Certificate.class, id)).thenReturn(null);
//        doThrow(new RuntimeException("Error during remove")).when(entityManager).remove(any());
//
//        assertThrows(PersistenceException.class, () -> certificateDao.delete(id));
//
//        verify(entityManager).remove(any());
//        verify(transaction).rollback();
//        verify(entityManager).close();
//    }

    @Test
    @DisplayName("Test find tags by Certificate id")
    void testFindTagsByCertificateId() {
        when(factory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.createEntityGraph(Certificate.class)).thenReturn(graph);
        when(entityManager.createQuery(anyString(), eq(Tag.class))).thenReturn(tagTypedQuery);
        when(tagTypedQuery.setParameter(anyString(), any())).thenReturn(tagTypedQuery);
        when(tagTypedQuery.getResultList()).thenReturn(expectedTags);
        List<Tag> actualTags = certificateDao.findTagsByCertificateId(id);
        assertEquals(expectedTags, actualTags);
        verify(entityManager).createQuery(anyString(), eq(Tag.class));
        verify(tagTypedQuery).setParameter(ID, id);
        verify(tagTypedQuery).getResultList();
    }

    @ParameterizedTest
    @CsvSource({
            "1, Winter, 10, Season",
            "2, Summer, 20, Season",
            "3, Spring, 30, Season",
            "4, Autumn, 40, Season"
    })
    @DisplayName("Test find tags by Certificate id")
    void testFindTagsByCertificateId(Long id, String name, Long id2, String namespace) {
        List<Tag> expectedTags = new ArrayList<>();
        expectedTags.add(Tag.builder().id(id).name(name).build());
        expectedTags.add(Tag.builder().id(id2).name(namespace).build());
        when(entityManager.createEntityGraph(Certificate.class)).thenReturn(graph);
        when(entityManager.createQuery(anyString(), eq(Tag.class))).thenReturn(tagTypedQuery);
        when(tagTypedQuery.setParameter(anyString(), any())).thenReturn(tagTypedQuery);
        when(tagTypedQuery.getResultList()).thenReturn(expectedTags);
        List<Tag> actualTags = certificateDao.findTagsByCertificateId(id);
        assertEquals(expectedTags, actualTags);
        verify(entityManager).createQuery(anyString(), eq(Tag.class));
        verify(tagTypedQuery).setParameter(anyString(), any());
        verify(tagTypedQuery).getResultList();
    }

    @Test
    @DisplayName("Populate expected Tag by Certificate id")
    void testFindTagsByCertificateIdExpected() {
        when(tagTypedQuery.getResultList()).thenReturn(expectedTags);
        when(entityManager.createQuery(anyString(), eq(Tag.class))).thenReturn(tagDtoTypedQuery);
        when(tagDtoTypedQuery.setParameter(anyString(), any())).thenReturn(tagDtoTypedQuery);
        when(tagDtoTypedQuery.getResultList()).thenReturn(expectedTags);
        List<Tag> actualTags = certificateDao.findTagsByCertificateId(id);
        assertEquals(expectedTags, actualTags);
        verify(factory).createEntityManager();
        verify(entityManager).createQuery(anyString(), eq(Tag.class));
        verify(tagDtoTypedQuery).setParameter(anyString(), any());
        verify(tagDtoTypedQuery).getResultList();
    }
}
