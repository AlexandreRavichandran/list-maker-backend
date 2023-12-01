package com.medialistmaker.music.utils;

import org.springframework.stereotype.Component;

@Component
public class TimeCalculator {

    public String formatSecondsToHourMinutesAndSeconds(int seconds) {

        int totalHours = (seconds % 86400) / 3600;
        int totalMinutes = ((seconds % 86400) % 3600) / 60;
        int totalSeconds = ((seconds % 86400) % 3600) % 60;

       return totalHours + "h" + totalMinutes + "m" + totalSeconds;

    }
}
