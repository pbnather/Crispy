package dk.au.itsmap.group4.crispy.service.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class NotificationAlarm {

    private static final long ONE_MINUTE_INTERVAL = 60 * 1000L; // 1 Minute

    public static void startAlarm(Context context) {
        // prepare intent
        Intent intent = new Intent(context, NotificationIntentService.class);
        PendingIntent alarmIntent = PendingIntent.getService(context, 0, intent, 0);

        // Set the alarm to start at approximately 9:00 a.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 9);

        // With setInexactRepeating(), you have to use one of the AlarmManager interval
        // constants--in this case, AlarmManager.INTERVAL_DAY.
        AlarmManager mAlarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (mAlarmMgr != null) {
            // TODO: change time to AlarmManager.INTERVAL_DAY for release version
            // this minute interval is only for debugging and testing 
            mAlarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), ONE_MINUTE_INTERVAL, alarmIntent);
        }
    }
}
