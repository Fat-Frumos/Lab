package com.epam.esm.generator;

import com.epam.esm.entity.Certificate;
import com.epam.esm.entity.Tag;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.regex.Pattern.compile;

@Slf4j
public class HtmlParser {
    private  HtmlParser(){

    }

    private static final Pattern DESC_PATTERN = compile("<div class=\"dek\">([^<]+)</div>");
    private static final Pattern NAME_PATTERN = compile("<h3 class=\"hed\"><a[^>]+>([^<]+)</a></h3>");
    private static final List<Certificate> CERTIFICATES = new ArrayList<>();
    private static final Set<String> NAMES = new HashSet<>();
    private static final Set<String> descriptions = new HashSet<>();
    public static List<String> links = new ArrayList<>();
    private static final List<Tag> tags = new ArrayList<>();
    private static final Set<String> tagNames = new HashSet<>();
    private static final List<String> stringSet = new ArrayList<>();

    public static void generate(String link, String pattern, String matches) {

        links.add(link);
        getTopic(link, pattern, matches);

//        parseFromUrls();

//        saveToFile("names.txt", names);
//        saveToFile("tag.txt", tagNames);
//        saveToFile("descriptions.txt", descriptions);
//        saveToFile("all.txt", stringSet);
    }

    private static void parseFromUrls() {
        links.add("https://www.apartmenttherapy.com/gift-card-ideas-265404");
        links.add("<h2.*>(.+?)</h2>");
        links.add("<a.*>([a-z].*?)</a>");
        links.add("<p>(.*?)</p>");

        links.add("https://nypost.com/article/best-e-gift-cards-online/");
        links.add("<a.*>([a-z].*?)<\\/a>");
        links.add("<span>(.+?)</span>");
        links.add("<p>(.*?)</p>");

        links.add("https://giftr.sg/pages/card-message-ideas");
        links.add("<span style=\"text-decoration: underline;\">(.*?)<\\/span>");
        links.add("<li>(.+?)</li>");
        links.add("<p>(.*?)</p>");

        links.add("https://a-perfect-present.myshopify.com/pages/gift-card-pharses");
        links.add("<strong>(.+?)</strong>");
        links.add("<li>(.+?)</li>");
        links.add("<p>(.*?)</p>");

        links.add("https://www.blossomshoppeflorist.com/card-message-ideas/");
        links.add("<h3>(.+?)</h3>");
        links.add("<li>(.+?)</li>");
        links.add("<p>(.*?)</p>");

        links.add("https://www.businessinsider.com/guides/gifts/best-gift-cards");
        links.add("<h2.*>(.+?)</h2>");
        links.add("<a.*>([a-z].*?)</a>");
        links.add("<p>(.*?)</p>");

        links.add("https://ideas.hallmark.com/articles/card-ideas/gift-card-message-ideas/");
        links.add("<h2 class=\"heading\">\\s*(.*?)\\s*<span");
        links.add("<li>(.+?)</li>");
        links.add("<p>(.+?)</p>");

        links.add("https://nymag.com/strategist/article/best-egift-cards-last-minute-gifts.html");
        links.add("<a href.*>([a-z].*?)<\\/a>");
        links.add("<span>(.+?)</span>");
        links.add("<p[^>]*>(.*?)</p>");

        links.add("https://www.harpersbazaar.com/fashion/trends/g38213692/best-gift-cards/");
        links.add("<div class=\\\"css-1gwdwp7 e7izsw30\\\">(.*?)</div>");
        links.add("<h2 class=\\\"css-vrgepf e8seki10\\\">(.*?)</h2>");
        links.add("<p>(.*?)</p>");

        for (int i = 0; i < links.size(); i += 4) {
            getFromHtml(i);
        }

        log.info(tags.stream().map(Tag::toString).collect(Collectors.joining(", ")));
        log.info(String.join(", ", tagNames));
        log.info(String.join(", ", NAMES));
        log.info(String.join(", ", descriptions));
        log.info(String.valueOf(tagNames.size()));
        log.info(String.valueOf(NAMES.size()));
        log.info(String.valueOf(descriptions.size()));

        stringSet.addAll(tagNames);
        stringSet.addAll(NAMES);
        stringSet.addAll(descriptions);
        stringSet.removeIf(String::isEmpty);

        IntStream.range(0, stringSet.size())
                .filter(i -> stringSet.get(i).equals(stringSet.get(i).toUpperCase()))
                .forEach(i -> stringSet.set(i, String.format("%s%s",
                        stringSet.get(i).substring(0, 1).toUpperCase(),
                        stringSet.get(i).substring(1).toLowerCase())));

        stringSet.sort(Comparator.comparingInt(s -> s.trim().split("\\s+").length));
    }

    private static void getFromHtml(int i) {
        String input = getString(i);
        addToList(compile(links.get(1 + i)).matcher(input), tagNames);
        addToList(compile(links.get(2 + i)).matcher(input), NAMES);
        addToList(compile(links.get(3 + i)).matcher(input), descriptions);
    }

    private static void addToList(Matcher matcher, Set<String> set) {
        while (matcher.find()) {
            if (!matcher.group(1).contains("$")) {
                set.add(matcher.group(1).replaceAll("\\<.*?\\>", ""));
            }
        }
    }

    private static String getString(int i) {
        try (InputStream in = new URL(links.get(i)).openStream()) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];

            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            return new String(out.toByteArray(), StandardCharsets.UTF_8)
                    .replaceAll("Message", "Gift")
                    .replaceAll("Card", "Certificate")
                    .replaceAll("&#8217;", "'")
                    .replaceAll("#\\d{4};|[^\\x00-\\x7F]|\\{\\{.*?\\}\\}|~|_", "")
                    .trim();
        } catch (Exception e) {
            System.err.printf("%s%n", e.getMessage());
        }
        return "";
    }

    public static void getTopic(String link, String pattern, String match) {
        Pattern aPattern = compile(pattern);
        Matcher liMatcher = aPattern.matcher(getString(0));

        while (liMatcher.find()) {
//            tags.add(Tag.builder().name(liMatcher.group(2)).build());
            links.add(liMatcher.group(1));
        }
        log.info("" + links.size());

        links = links.stream()
                .filter(l -> l.contains(match))
                .collect(Collectors.toList());

        Set<String> holiday = new HashSet<>();
        for (int i = 1; i < links.size(); i++) {
            Matcher matcher = Pattern.compile("<a class=\"country-listing\".*?>(.*?)<\\/a>").matcher(getString(i));
            while (matcher.find()) {
                holiday.add(matcher.group(1));
            }
        }

//        IntStream.range(1, tags.size()).parallel()
//                .filter(i -> String.valueOf(links.get(i))
//                        .matches(match))
//                .forEach(i -> getCertificate(getString(i), tags.get(i)));
//        log.info(String.valueOf(certificates.size()));
    }

    private static void getCertificate(String input, Tag tag) {
        Matcher nameMatcher = NAME_PATTERN.matcher(input);
        Matcher descMatcher = DESC_PATTERN.matcher(input);
        while (nameMatcher.find() && descMatcher.find()) {
            Certificate build = Certificate.builder()
                    .name(nameMatcher.group(1))
                    .description(descMatcher.group(1))
                    .build().addTag(tag);
            CERTIFICATES.add(build);
            log.info(String.valueOf(build));
        }
    }
}
