package com.epam.esm.generator;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import com.epam.esm.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URI;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Slf4j
@Component
public class EntityBuilder {
    private static final Random RANDOM = new Random();
    private static final String host = "https://raw.githubusercontent.com/Fat-Frumos/giftstore/develop/api/src/test/resources/";
    private static final List<String> BRANDS = extractFromUrl(host + "brands.txt");
    private static final List<String> TAGS = extractFromUrl(host + "tags.txt");
    private static final List<String> HOLIDAY = extractFromUrl(host + "holiday.txt");
    private static final List<String> NAMES = extractFromUrl(host + "giftnames.txt");
    private static final List<String> DESC = extractFromUrl(host + "desc.txt");
    private static final List<String> USERNAMES = extractFromUrl(host + "usernames.txt");
    private static final List<Certificate> certificates = new ArrayList<>();

    public static String getRandomElement(List<String> list) {
        return list.get(getRandomInt(0, RANDOM.nextInt(list.size())));
//        return list.remove(getRandomInt(0, RANDOM.nextInt(list.size())));
    }
    public static List<String> extractFromUrl(
            final String url) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(URI.create(url).toURL().openStream()))) {
            lines = reader.lines().collect(toList());
        } catch (IOException e) {
            log.error(String.format("%s%nFile not found: %s%n", e.getMessage(), url));
        }
        return lines;
    }

    public static String generateDesc() {
        String brand = getRandomElement(BRANDS);
        String desc = getRandomElement(DESC);
        return String.format("%s %s", desc, brand);
    }

    public static String generateName() {
        String name = getRandomElement(NAMES);
        String holiday = getRandomElement(HOLIDAY);
        return String.format("%s %s", name, holiday);
    }

    public static int getRandomInt(final int min, final int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

    public static List<Certificate> generateCertificates(final int size) {
        IntStream.range(0, size)
                .mapToObj(i -> Certificate.builder()
                        .id(UUID.randomUUID().getMostSignificantBits())
                        .name(generateName())
                        .createDate(Timestamp.valueOf(LocalDateTime.now()))
                        .lastUpdateDate(Timestamp.valueOf(LocalDateTime.now()))
                        .duration(getRandomInt(10, 30))
                        .price(BigDecimal.valueOf(getRandomInt(10, 100)))
                        .description(generateDesc())
                        .tags(generateTags())
                        .build())
                .forEach(certificates::add);
        return certificates;
    }

    private static Set<Tag> generateTags() {
        return IntStream.range(0, RANDOM.nextInt(3) + 1)
                .mapToObj(j -> Tag.builder().name(TAGS.get(RANDOM.nextInt(TAGS.size()))).build())
                .collect(toSet());
    }

    public static List<User> generateUsers(
            final int max) {
        return IntStream.range(0, max)
                .mapToObj(i -> User.builder()
                        .username(getRandomElement(USERNAMES))
                        .email(getRandomElement(USERNAMES) + "@i.ua")
                        .build())
                .collect(toList());
    }
}

