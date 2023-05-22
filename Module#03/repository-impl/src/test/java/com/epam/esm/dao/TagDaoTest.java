package com.epam.esm.dao;

import com.epam.esm.entity.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TagDaoTest {

    @Mock
    private EntityManagerFactory entityManagerFactory;
    @Mock
    private EntityManager entityManager;
    @Mock
    private EntityTransaction entityTransaction;
    @Mock
    private CriteriaQuery<Tag> query;
    @Mock
    private Root<Tag> root;
    @Mock
    private TypedQuery<Tag> typedQuery;
    @Mock
    CriteriaBuilder builder;
    private TagDaoImpl tagDao;
    private final Pageable pageable = PageRequest.of(0, 25, Sort.by("name").ascending());
    private final Long id = 1L;
    private final String tagName = "Spring";
    private final Tag tag = Tag.builder().id(id).name(tagName).build();
    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        root = mock(Root.class);
        query = mock(CriteriaQuery.class);
        typedQuery = mock(TypedQuery.class);
        builder = mock(CriteriaBuilder.class);
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        tagDao = new TagDaoImpl(entityManagerFactory);
    }

    @ParameterizedTest
    @CsvSource({
            "1, Winter",
            "2, Summer",
            "3, Spring",
            "4, Autumn"
    })
    @DisplayName("Test save method")
    void testSave(Long id, String name) {
        Tag tag = Tag.builder().id(id).name(name).build();
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        Tag result = tagDao.save(tag);
        assertEquals(tag, result);
        verify(entityManager).persist(tag);
        verify(entityTransaction).commit();
        verify(entityManager).close();
    }

    @ParameterizedTest
    @CsvSource({
            "1, Winter",
            "2, Summer",
            "3, Spring",
            "4, Autumn"
    })
    @DisplayName("Test getByName method")
    void testGetByName(Long id, String name) {
        Tag tag = Tag.builder().id(id).name(name).build();
        List<Tag> tags = singletonList(tag);
        when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        when(entityManager.createQuery(query)).thenReturn(typedQuery);
        when(query.from(Tag.class)).thenReturn(root);
        when(builder.createQuery(Tag.class)).thenReturn(query);
        when(builder.equal(root.get("name"), name)).thenReturn(mock(Predicate.class));
        when(typedQuery.setMaxResults(1)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(tags);
        Optional<Tag> result = tagDao.getByName(name);
        assertTrue(result.isPresent());
        assertEquals(tag, result.get());

        verify(entityManager).getCriteriaBuilder();
        verify(entityManager).createQuery(query);
        verify(builder).createQuery(Tag.class);
        verify(builder).equal(root.get("name"), name);

        ArgumentCaptor<Predicate> predicateCaptor = ArgumentCaptor.forClass(Predicate.class);
        verify(query).where(predicateCaptor.capture());
        verify(query).from(Tag.class);
        verify(typedQuery).setMaxResults(1);
        verify(typedQuery).getResultList();
    }

    @ParameterizedTest
    @CsvSource({
            "1, Winter",
            "2, Summer",
            "3, Spring",
            "4, Autumn"
    })
    @DisplayName("Test get By Id method")
    void testGetById(Long id, String name) {
        Tag tag = Tag.builder().id(id).name(name).build();
        when(entityManager.find(Tag.class, id)).thenReturn(tag);
        Optional<Tag> result = tagDao.getById(id);
        assertTrue(result.isPresent());
        assertEquals(tag, result.get());
    }

    @ParameterizedTest
    @CsvSource({
            "1, Winter",
            "2, Summer",
            "3, Spring",
            "4, Autumn"
    })
    @DisplayName("Test getById with valid id")
    void testGetByIdWithValidId(Long id, String name) {
        Tag tag = Tag.builder().id(id).name(name).build();
        when(entityManager.find(Tag.class, id)).thenReturn(tag);
        Optional<Tag> result = tagDao.getById(id);
        assertTrue(result.isPresent());
        assertEquals(tag, result.get());
        verify(entityManager).find(Tag.class, id);
    }

    @Test
    @DisplayName("Test getById with invalid id")
    void testGetByIdWithInvalidId() {
        Long id = 1L;
        when(entityManager.find(Tag.class, id)).thenReturn(null);
        Optional<Tag> result = tagDao.getById(id);
        assertFalse(result.isPresent());
        verify(entityManager).find(Tag.class, id);
    }

    @ParameterizedTest
    @CsvSource({
            "1, Winter",
            "2, Summer",
            "3, Spring",
            "4, Autumn"
    })
    @DisplayName("Test save method with existing tag")
    void testSaveWithExistingTag(Long id, String name) {
        Tag tag = Tag.builder().id(id).name(name).build();
        EntityManager entityManager = mock(EntityManager.class);
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        doThrow(EntityExistsException.class).when(entityManager).persist(tag);
        assertThrows(PersistenceException.class, () -> tagDao.save(tag));
        verify(entityTransaction).begin();
        verify(entityTransaction, never()).commit();
        verify(entityManager).close();
    }

    @DisplayName("Test getAll method")
    @ParameterizedTest
    @CsvSource({
            "1, Winter",
            "2, Summer",
            "3, Spring",
            "4, Autumn"
    })
    void testGetAll(Long id, String name) {

        Tag tag = Tag.builder().id(id).name(name).build();
        List<Tag> tags = Arrays.asList(
                Tag.builder().id(1L).name("Winter").build(),
                Tag.builder().id(2L).name("Summer").build(),
                Tag.builder().id(3L).name("Spring").build(),
                Tag.builder().id(4L).name("Autumn").build()
        );

        when(entityManager.getCriteriaBuilder()).thenReturn(builder);
        when(builder.createQuery(Tag.class)).thenReturn(query);
        when(entityManager.createQuery(query)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(tags);
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(typedQuery.setFirstResult(pageable.getPageNumber() * pageable.getPageSize())).thenReturn(typedQuery);
        when(typedQuery.setMaxResults(pageable.getPageSize())).thenReturn(typedQuery);

        List<Tag> result = tagDao.getAll(pageable);

        assertEquals(tags, result);

        assertEquals(tag, result.get(Math.toIntExact(tag.getId() - 1)));

        verify(entityManagerFactory).createEntityManager();
        verify(entityManager).getCriteriaBuilder();
        verify(builder).createQuery(Tag.class);
        verify(query).from(Tag.class);
        verify(query).select(query.from(Tag.class));
        verify(entityManager).createQuery(query);
        verify(typedQuery).getResultList();
        verify(entityManager).close();
    }

    @ParameterizedTest
    @CsvSource({
            "1, Winter",
            "2, Summer",
            "3, Spring",
            "4, Autumn"
    })
    @DisplayName("Test delete method with valid ID")
    void testDeleteWithValidId(Long id) {
        Tag entity = mock(Tag.class);
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getReference(Tag.class, id)).thenReturn(entity);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        tagDao.delete(id);
        verify(entityManager, times(1)).remove(entity);
        verify(entityTransaction, times(1)).begin();
        verify(entityTransaction, times(1)).commit();
        verify(entityManager, times(1)).close();
    }

    @Test
    @DisplayName("Test delete method with throws Persistence Exception")
    void testDeleteThrowsPersistenceException() {
        Tag entity = mock(Tag.class);
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityManager.getReference(Tag.class, id)).thenReturn(entity);
        doThrow(PersistenceException.class).when(entityManager).remove(entity);

        doNothing().when(entityTransaction).begin();
        when(entityTransaction.isActive()).thenReturn(true);
        doNothing().when(entityTransaction).rollback();

        assertThrows(PersistenceException.class, () -> tagDao.delete(id));

        verify(entityTransaction).begin();
        verify(entityTransaction).rollback();
        verify(entityManager).getTransaction();
        verify(entityManager).close();
    }


    @Test
    @DisplayName("Test save method with throws Persistence Exception")
    void testSaveException() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.getTransaction()).thenReturn(entityTransaction);
        when(entityTransaction.isActive()).thenReturn(true);
        doThrow(new RuntimeException("Error during persist")).when(entityManager).persist(tag);
        PersistenceException exception = assertThrows(PersistenceException.class, () -> tagDao.save(tag));
        assertEquals("Error during persist", exception.getMessage());
        verify(entityTransaction).rollback();
        verify(entityManager).close();
    }

    @Test
    @DisplayName("Test Get By Id method Success")
    void testGetById() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.find(Tag.class, id)).thenReturn(tag);
        Optional<Tag> result = tagDao.getById(id);
        assertTrue(result.isPresent());
        assertEquals(tag, result.get());
    }

    @Test
    @DisplayName("Test Get By Id method with throws Runtime Exception")
    void testGetByIdException() {
        when(entityManagerFactory.createEntityManager()).thenReturn(entityManager);
        when(entityManager.find(Tag.class, id)).thenThrow(new RuntimeException("Error during find"));
        Optional<Tag> result = tagDao.getById(id);
        assertFalse(result.isPresent());
    }
}
