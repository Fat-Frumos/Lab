package com.epam.esm.generator;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.generator.EntityBuilder.generateCertificates;
import static com.epam.esm.generator.EntityBuilder.generateUsers;
import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CertificateDao certificateDao;
    private final UserDao userDao;

    @Override
    @Transactional
    public void run(String... args) {
        List<Certificate> certificates = generateCertificates(1000);
        log.info(certificates.size() + " have been created and prepared for preservation");
        try {
            List<Certificate> list = certificates.stream() //.parallel()
                    .map(certificateDao::save)
                    .collect(toList());
            log.info(list.size() + " certificates were successfully saved to the storage");
        } catch (Exception e) {
            log.error("Data integrity violation: " + e.getMessage());
        }

        List<User> users = generateUsers(50);
        List<User> userList = users.stream().map(userDao::save).collect(toList());

        log.info(users.size() + " have been created and prepared for preservation");
        log.info(userList.size() + " users were successfully saved to the storage");
    }
}
