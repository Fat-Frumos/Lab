package com.epam.esm.security.service;

import com.epam.esm.entity.Token;
import com.epam.esm.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

//@ExtendWith(SpringExtension.class)
//@DataJpaTest
//class JpaTokenProviderTest {
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Autowired
//    private TokenRepository tokenRepository;
//
//    @Test
//    public void testSave() {
//        Token token = new Token();
//        token.setAccessToken("accessToken");
//        token.setAccessTokenTTL(1000L);
//        token.setRevoked(false);
//        token.setExpired(false);
//
//        Token savedToken = tokenRepository.save(token);
//
//        assertNotNull(savedToken);
//        assertNotNull(savedToken.getId());
//        assertEquals(token.getAccessToken(), savedToken.getAccessToken());
//        assertEquals(token.getAccessTokenTTL(), savedToken.getAccessTokenTTL());
//        assertEquals(token.isRevoked(), savedToken.isRevoked());
//        assertEquals(token.isExpired(), savedToken.isExpired());
//    }
//}