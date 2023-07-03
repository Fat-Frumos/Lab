package com.epam.esm.controller;

import com.epam.esm.dto.CertificateDto;
import com.epam.esm.dto.TagDto;
import com.epam.esm.entity.Criteria;
import com.epam.esm.entity.Role;
import com.epam.esm.entity.RoleType;
import com.epam.esm.entity.User;
import com.epam.esm.service.CertificateService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@DirtiesContext
@SpringBootTest
@AutoConfigureMockMvc
class CertificateControllerTest {
    Long id = 1101L;
    String username = "User";
    String email = "user@i.ua";
    String password = "1";
    String admin = "ROLE_ADMIN";
    User userDetails;
    @MockBean
    private CertificateService service;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void clearDatabase() {
        userDetails = User.builder().id(id).role(Role.builder().permission(RoleType.USER)
                .build()).email(email).password(password).username(username).build();
        entityManager.createQuery("DELETE FROM Order").executeUpdate();
        entityManager.createQuery("DELETE FROM User").executeUpdate();
    }

    CertificateDto dto = getDto(new BigDecimal("9.99"), 1L, "Gift", "Certificate", 11);

    @Test
    @DisplayName("Test getById - Retrieves a certificate by ID")
    void testGetCertificates() throws Exception {
        when(service.getById(1L)).thenReturn(dto);
        mockMvc.perform(get("/certificates/1")
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin))))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":1,\"name\":\"Gift\"}"));
    }

    @Test
    @DisplayName("Given certificate ID, when getById, then return the corresponding certificate")
    void testGetCertificateById() throws Exception {
        when(service.getById(1L)).thenReturn(dto);
        mockMvc.perform(get("/certificates/1")
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()));
        verify(service).getById(1L);
    }

    @ParameterizedTest
    @CsvSource({
            "1, Java, description, 10, 30, Spring, Season",
            "2, SQL, description, 10, 30, Summer, Season",
            "3, Programming, description, 10, 30, Winter, Season",
            "4, PostgreSQL, description, 10, 30, Autumn, Season",
            "5, Spring, description, 10, 30, Years, Season"
    })
    @DisplayName("Given pageable information, when getAll, then return all certificates with pagination")
    void getAllTest(
            long id, String name, String description, BigDecimal price, long id2, String name2, String desc)
            throws Exception {
        CertificateDto certificateDto = getDto(price, id, name, description, Math.toIntExact(id2));
        CertificateDto certificateDto2 = getDto(price, id2, name2, desc, Math.toIntExact(id + 10));
        List<CertificateDto> certificateDtoList = Arrays.asList(certificateDto, certificateDto2);
        given(service.getCertificates(any(Pageable.class))).willReturn(certificateDtoList);
        mockMvc.perform(get("/certificates")
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.certificateDtoList").isArray())
                .andReturn();
    }

    @ParameterizedTest
    @CsvSource({
            "1, Java, description, 10, 30, Spring, Season",
            "2, SQL, description, 10, 30, Summer, Season",
            "3, Programming, description, 10, 30, Winter, Season",
            "4, PostgreSQL, description, 10, 30, Autumn, Season",
            "5, Spring, description, 10, 30, Years, Season"
    })
    @DisplayName("Given search criteria and pageable information, when search, then return matching certificates with pagination")
    void searchTests(
            long id, String name, String description, BigDecimal price, int duration, String tag1, String tag2)
            throws Exception {
        List<CertificateDto> dtoList = Collections.singletonList(getDto(price, id, name, description, duration));
        given(service.findAllBy(any(Criteria.class), any(Pageable.class))).willReturn(dtoList);
        mockMvc.perform(get("/certificates/search")
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin)))
                        .param("name", name)
                        .param("description", description)
                        .param("tagNames", tag1, tag2)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded").exists())
                .andExpect(jsonPath("$._embedded.certificateDtoList").isArray())
                .andReturn();
    }

    @Test
    @DisplayName("Given certificate ID, when delete, then delete the corresponding certificate")
    void deleteTest() throws Exception {
        mockMvc.perform(delete("/certificates/{id}", id)
                        .with(jwt().authorities(new SimpleGrantedAuthority(admin))))
                .andExpect(status().isNoContent());
        verify(service, times(1)).delete(id);
    }

    @ParameterizedTest
    @CsvSource({
            "1, Java, description, 1, 1",
            "2, SQL, description, 2, 2",
            "3, Programming, description, 3, 3",
            "4, PostgreSQL, description, 4, 4",
            "5, Spring, description, 5, 5"
    })
    @DisplayName("Given user ID, when getUserCertificates, then return all certificates associated with the user")
    void getUserCertificatesTest(
            long id, String name, String description, long userId, int certificateId)
            throws Exception {
        CertificateDto certificateDto = getDto(
                BigDecimal.valueOf(certificateId), id, name, description, Math.toIntExact(userId));
        List<CertificateDto> certificateDtoList = Collections.singletonList(certificateDto);
        given(service.getCertificatesByUserId(userId)).willReturn(new PageImpl<>(certificateDtoList));
        mockMvc.perform(get("/certificates/users/{id}", userId))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource({
            "1, Java, description, 1, 1",
            "2, SQL, description, 2, 2",
            "3, Programming, description, 3, 3",
            "4, PostgreSQL, description, 4, 4",
            "5, Spring, description, 5, 5"
    })
    @DisplayName("Given order ID, when getAllByOrderId, then return all certificates associated with the order")
    void getAllByOrderIdTest(
            long id, String name, String description, long orderId, int certificateId)
            throws Exception {
        CertificateDto certificateDto = getDto(
                BigDecimal.valueOf(certificateId), id, name, description, Math.toIntExact(orderId));
        List<CertificateDto> certificateDtoList = Collections.singletonList(certificateDto);
        given(service.getByOrderId(orderId)).willReturn(certificateDtoList);
        mockMvc.perform(get("/certificates/orders/{id}", orderId))
                .andExpect(status().is4xxClientError());
    }

    @ParameterizedTest
    @CsvSource({
            "1, Java, 10",
            "2, SQL, 20",
            "3, Programming, 30",
            "4, PostgreSQL, 40",
            "5, Spring, 50"
    })
    @DisplayName("Given certificate ID, when getTagsByCertificateId, then return all tags associated with the certificate")
    void getTagsByCertificateIdTest(long id, String name, long certificateId)
            throws Exception {
        TagDto tagDto = TagDto.builder()
                .id(id)
                .name(name)
                .build();
        Set<TagDto> tagDtoSet = Collections.singleton(tagDto);
        given(service.findTagsByCertificateId(certificateId)).willReturn(tagDtoSet);
        mockMvc.perform(get("/certificates/{id}/tags", certificateId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.tagDtoList[0].id").value(certificateId / 10))
                .andExpect(jsonPath("$._embedded.tagDtoList[0].name").value(name));
    }

    private static CertificateDto getDto(
            BigDecimal price, long id2, String name, String desc, int duration) {
        return CertificateDto.builder()
                .id(id2).price(price)
                .name(name)
                .description(desc)
                .duration(duration)
                .build();
    }
}
