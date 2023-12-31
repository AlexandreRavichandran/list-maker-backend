package com.medialistmaker.music.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeezerParameterFormatterTest {

    @Autowired
    DeezerParameterFormatter deezerFormatter;

    @BeforeEach
    void beforeAllTests() {
        this.deezerFormatter = new DeezerParameterFormatter();
    }

    @Test
    void givenMapWithParamShouldReturnFormattedDeezerQueryString() {

        Map<String, String> params = new HashMap<>();
        params.put("firstParam", "1");
        params.put("secondParam", "2");
        params.put("thirdParam", "3");

        String testGetQueryString = this.deezerFormatter.formatParams(params);

        assertEquals("firstParam:\"1\" thirdParam:\"3\" secondParam:\"2\" ", testGetQueryString);

    }

    @Test
    void givenMapWithParamsWithNullValuesShouldReturnFormattedDeezerQueryStringWithNonNullParams() {

        Map<String, String> params = new HashMap<>();
        params.put("firstParam", "1");
        params.put("thirdParam", null);
        params.put("secondParam", "2");

        String testGetQueryString = this.deezerFormatter.formatParams(params);

        assertEquals("firstParam:\"1\" secondParam:\"2\" ", testGetQueryString);

    }
}