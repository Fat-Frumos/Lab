package com.epam.esm.generator;

import com.epam.esm.dao.CertificateDao;
import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.epam.esm.generator.EntityBuilder.generateCertificates;
import static com.epam.esm.generator.EntityBuilder.generateUsers;
import static com.epam.esm.generator.EntityBuilder.getRandomInt;
import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@AllArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CertificateDao certificateDao;
    private final UserDao userDao;
    private final OrderDao orderDao;

    @Override
    @Transactional
    public void run(String... args) {
        List<Certificate> certificates = generateCertificates(500);
        log.info(certificates.size() + " have been created and prepared for preservation");
        List<Certificate> certificateList = new ArrayList<>();
        try {
            certificateList = certificates.stream() //.parallel()
                    .map(certificateDao::save).collect(toList());
            log.info(certificateList.size() + " certificates were successfully saved to the storage");
        } catch (Exception e) {
            log.error("Data integrity violation: " + e.getMessage());
        }

        List<User> users = generateUsers(100);
        List<User> userList = users.stream().map(userDao::save).collect(toList());

        List<Order> orderList = new ArrayList<>();

        log.info(users.size() + " have been created and prepared for preservation");
        log.info(userList.size() + " users were successfully saved to the storage");

        for (User user : userList) {
            User managedUser = userDao.getById(user.getId()).orElse(null);
            if (managedUser != null) {
                Collections.shuffle(certificateList);
                Set<Certificate> selectedCertificates = new HashSet<>(certificateList.subList(0, getRandomInt(0, 5)));
                Order order = Order.builder()
                        .user(user)
                        .certificates(selectedCertificates)
                        .cost(selectedCertificates.stream()
                                .map(Certificate::getPrice)
                                .reduce(BigDecimal.ZERO, BigDecimal::add))
                        .build();
                orderList.add(orderDao.save(order));
                managedUser.addOrder(order);
                // TODO user.update
            }
        }
        log.info(orderList.size() + " orders were successfully saved to the storage");
    }
}
