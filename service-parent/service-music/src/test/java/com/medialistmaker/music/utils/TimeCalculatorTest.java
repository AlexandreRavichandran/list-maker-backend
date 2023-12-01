package com.medialistmaker.music.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeCalculatorTest {

    @Test
    void givenTotalSecondWhenFormatSecondsToHourMinutesAndSecondsShouldReturnTimeInHourMinutesAndSecond() {

        TimeCalculator timeCalculator = new TimeCalculator();

        String testFormatSeconds = timeCalculator.formatSecondsToHourMinutesAndSeconds(3600);

        assertEquals("1h0m0", testFormatSeconds);
    }
}