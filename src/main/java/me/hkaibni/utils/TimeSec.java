package me.hkaibni.utils;

import java.time.LocalDateTime;

public class TimeSec {

    public static LocalDateTime getDailyResetTime() {

        LocalDateTime now = LocalDateTime.now();

        LocalDateTime resetTime = now
                .withHour(2)
                .withMinute(0)
                .withSecond(0)
                .withNano(0);

        // If current time is before today's 2:00 AM,
        // use yesterday's 2:00 AM.
        if (now.isBefore(resetTime)) {
            resetTime = resetTime.minusDays(1);
        }

        return resetTime;
    }
}