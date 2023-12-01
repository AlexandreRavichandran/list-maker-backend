package com.medialistmaker.music.utils;

import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MathUtilsTest {

    @Test
    void givenListOfIntWhenCalculateAverageOfListShouldAverageOfListValues() {

        MathUtils mathUtils = new MathUtils();

        int[] numberList = Stream.of(1, 2, 3).mapToInt(Integer::intValue).toArray();

        Integer testGetAverage = mathUtils.calculateAverageOfList(numberList);

        assertEquals(2, testGetAverage);
    }
}