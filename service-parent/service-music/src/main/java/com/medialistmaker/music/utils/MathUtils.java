package com.medialistmaker.music.utils;

import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class MathUtils {

    public Integer calculateAverageOfList(int[] numbers) {

        int sumOfNumbers = Arrays.stream(numbers).sum();

        return sumOfNumbers / numbers.length;
    }
}
