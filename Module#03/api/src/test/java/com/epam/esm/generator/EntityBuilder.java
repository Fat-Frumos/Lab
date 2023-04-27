package com.epam.esm.generator;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

@Component
public class EntityBuilder {
    private static final Random RANDOM = new Random();
    private static final List<String> BRANDS = extractFromUrl("https://raw.githubusercontent.com/Fat-Frumos/giftstore/develop/api/src/test/resources/brands.txt");
    private static final List<String> TAGS = extractFromUrl("https://raw.githubusercontent.com/Fat-Frumos/giftstore/develop/api/src/test/resources/tags.txt");
    private static final List<String> NAMES = extractFromUrl("https://raw.githubusercontent.com/Fat-Frumos/giftstore/develop/api/src/test/resources/giftnames.txt");
    public static final List<String> USERNAMES = extractFromUrl("https://raw.githubusercontent.com/Fat-Frumos/giftstore/develop/api/src/test/resources/usernames.txt");
    public static final List<String> DESC = extractFromUrl("https://raw.githubusercontent.com/Fat-Frumos/giftstore/develop/api/src/test/resources/desc.txt");
    private static final List<Certificate> certificates = new ArrayList<>();

    public static String getRandomElement(List<String> list) {
        return list.remove(getRandomInt(0, RANDOM.nextInt(list.size())));
    }

    public static List<String> extractFromUrl(String url) {
        try {
            return new BufferedReader(
                    new InputStreamReader(new URL(url)
                            .openStream()))
                    .lines().collect(toList());
        } catch (IOException e) {
            System.err.printf("%s file not found: %s%n", e.getMessage(), url);
        }
        return new ArrayList<>();
    }

    public static String generateDesc() {
        String brand = getRandomElement(BRANDS);
        String tag = getRandomElement(TAGS);
        String name = getRandomElement(NAMES);
        String desc = getRandomElement(DESC);
        return String.format("%s %s %s for %s", name, brand, tag, desc);
    }
    public static int getRandomInt(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }
    public static List<Certificate> generateCertificates(final int size) {
        IntStream.range(0, size)
                .mapToObj(i -> Certificate.builder()
                        .name(getRandomElement(NAMES))
                        .createDate(Timestamp.valueOf(LocalDateTime.now()))
                        .lastUpdateDate(Timestamp.valueOf(LocalDateTime.now()))
                        .duration(getRandomInt(10, 30))
                        .price(BigDecimal.valueOf(getRandomInt(10, 100)))
                        .description(generateDesc())
                        .tags(setTags()).build())
                .forEach(certificates::add);
        return certificates;
    }

    private static Set<Tag> setTags() {
        return IntStream.range(0, RANDOM.nextInt(3) + 1)
                .mapToObj(j -> Tag.builder().name(TAGS.get(j)).build())
                .collect(toSet());
    }

//    public static List<User> generateUsers(
//            final int max) {
//        return IntStream.range(0, max)
//                .mapToObj(i -> User.builder()
//                        .username(getRandomElement(USERNAMES))
//                        .email(getRandomElement(USERNAMES) + "@i.ua")
//                        .build())
//                .collect(toList());
//    }
}

