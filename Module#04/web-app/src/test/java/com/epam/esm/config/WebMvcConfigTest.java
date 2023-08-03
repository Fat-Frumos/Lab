package com.epam.esm.config;

import com.epam.esm.entity.SecurityUser;
import com.epam.esm.entity.User;
import com.epam.esm.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@WebAppConfiguration
@RunWith(SpringRunner.class)
class WebMvcConfigTest {

    private User user;
    private SecurityUser securityUser;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @BeforeEach
    public void setup() {
        user = User.builder().id(1L).username("user").email("email@email.ua").password("password").build();
        securityUser = SecurityUser.builder().user(user).build();
    }

    @Test
    void testLoadUserByUsername() {
        String username = "test";
        user.setUsername(username);
        userRepository.save(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        assertEquals(username, userDetails.getUsername());
    }
}
