package com.epam.esm.generator;


import com.epam.esm.dao.CertificateDao;
import com.epam.esm.entity.Certificate;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.epam.esm.generator.EntityBuilder.generateCertificates;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

//    private UserDao userRepository;

    private CertificateDao certificateRepository;

    @Override
    public void run(String... args) {
//        List<User> users = generateUsers(10);
//        assertFalse(users.isEmpty());
        List<Certificate> certificates = generateCertificates(10);
        assertFalse(certificates.isEmpty());
        for (Certificate certificate : certificates) {
            System.out.println(certificate);
//            Certificate save = certificateRepository.save(certificate);
        }
//        Iterable<Certificate> saveAll = certificateRepository.saveAll(certificates);
//        System.out.println(saveAll);

//        Iterable<User> users1 = userRepository.saveAll(users);
//        System.out.println(users1);
//
//            List<User> users = generateUsers(10);
//            assertFalse(users.isEmpty());
//            List<Certificate> certificates = generateCertificates(10);
//            assertFalse(certificates.isEmpty());
//
//            Iterable<User> users1 = userRepository.saveAll(users);
//            System.out.println(users1);
//        }
    }
}
