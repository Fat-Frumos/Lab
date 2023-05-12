package com.epam.esm.generator;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.entity.Certificate;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.epam.esm.generator.EntityBuilder.generateCertificates;
import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private CertificateDao certificateDao;

    @Override
    @Transactional
    public void run(String... args) {
        List<Certificate> certificates = generateCertificates(1000);
        log.info(certificates.size() + " certificates are prepared for storage");
        try {
            List<Certificate> list = certificates.stream() //.parallel()
                    .map(certificate -> certificateDao.save(certificate))
                    .collect(toList());
            log.info(list.size() + " certificates were successfully generated");
        } catch (Exception e) {
            log.error("Data integrity violation: " + e.getMessage());
        }
    }
}

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
