package me.hkaibni.security;

import java.util.Calendar;
import java.util.Date;

public class TimeSec {


    public static Date getDailyResetTime() {

        Calendar calendar = Calendar.getInstance();

        // Set the time to today's 2:00 AM
        calendar.set(Calendar.HOUR_OF_DAY, 2);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        Date resetTime = calendar.getTime();

        // If the current time is before today's 2 AM,
        // use yesterday's 2 AM instead.
        if (new Date().before(resetTime)) {
            calendar.add(Calendar.DAY_OF_MONTH, -1);
            resetTime = calendar.getTime();
        }

        return resetTime;
    }
}
