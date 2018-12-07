package dk.au.itsmap.group4.crispy.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Inspired by:
 * https://stackoverflow.com/a/34039097
 */
public class TimeUtils {

    public static boolean isToday(Date date) {
        Calendar now = Calendar.getInstance();
        Calendar timeToCheck = Calendar.getInstance();
        timeToCheck.setTime(date);
        return (now.get(Calendar.YEAR) == timeToCheck.get(Calendar.YEAR)
                && now.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR));
    }

    public static boolean isTomorrow(Date date) {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.DAY_OF_MONTH, 1);
        Calendar timeToCheck = Calendar.getInstance();
        timeToCheck.setTime(date);
        return (now.get(Calendar.YEAR) == timeToCheck.get(Calendar.YEAR)
                && now.get(Calendar.DAY_OF_YEAR) == timeToCheck.get(Calendar.DAY_OF_YEAR));
    }
}
