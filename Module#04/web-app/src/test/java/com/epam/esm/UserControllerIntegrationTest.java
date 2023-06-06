//package com.epam.esm;
//
//import com.epam.esm.dao.UserDao;
//import com.epam.esm.entity.User;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.DirtiesContext;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@DirtiesContext
//@RunWith(SpringRunner.class)
//@SpringBootTest
//@Transactional
//@AutoConfigureMockMvc
//class UserControllerIntegrationTest {
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @BeforeEach
//    void clearDatabase() {
//        entityManager.createQuery("DELETE FROM Order").executeUpdate();
//        entityManager.createQuery("DELETE FROM User").executeUpdate();
//    }
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserDao userDao;
//
//    User admin = User.builder().email("admin@i.ua").username("Admin").build();
//    User user = User.builder().email("user@i.ua").username("User").build();
//
//    @Test
//    void getUser() throws Exception {
//        mockMvc.perform(get("/users/1100"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1100))
//                .andExpect(jsonPath("$.username").value("Admin"));
//    }
//
//    @Test
//    void getAllBy() throws Exception {
//        User saved = userDao.save(user);
//        mockMvc.perform(get("/users?page=40"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$._embedded.userDtoList", hasSize(2)))
//                .andExpect(jsonPath("$._embedded.userDtoList[0].id").value(1100))
//                .andExpect(jsonPath("$._embedded.userDtoList[0].username").value("Admin"))
//                .andExpect(jsonPath("$._embedded.userDtoList[1].id").value(1101))
//                .andExpect(jsonPath("$._embedded.userDtoList[1].username").value("User"));
//        assertEquals(saved, user);
//    }
//
//    @Test
//    void getById() throws Exception {
//        User save = userDao.save(admin);
//        mockMvc.perform(get("/users/1100"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id").value(1100))
//                .andExpect(jsonPath("$.email").value("admin@i.ua"))
//                .andExpect(jsonPath("$.username").value("Admin"));
//        assertEquals(save, admin);
//    }
//}
