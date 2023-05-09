package com.epam.esm.generator;

import org.junit.jupiter.api.Test;

class HtmlParserTest {

    @Test
    void testGetTopic() {

//        HtmlParser.generate("https://hbr.org/topics", "<li><a\\s+href=\"([^\"]+)\">([^<]+)</a></li>", ".*topic/subject/.*");

        String link = "https://www.officeholidays.com/countries";
        String pattern = "<a href=\"(.*?)\"";
        String match = "/countries/";
//        HtmlParser.generate(link, pattern, match);
    }
}

