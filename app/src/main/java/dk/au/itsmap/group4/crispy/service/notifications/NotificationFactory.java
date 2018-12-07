package dk.au.itsmap.group4.crispy.service.notifications;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.ui.MainNavigationActivity;

/**
 * Class for creating notifications
 */
public class NotificationFactory
{
    private String CHANNEL_ID = "crispy_app_channel";
    private Context mContext;

    public NotificationFactory(Context context) {

        this.mContext = context;

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


    }

    public void notifyAboutTodaysMeals() {
        Intent intent = new Intent(this.mContext, MainNavigationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.mContext, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this.mContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.crispy_launcher_round)
                .setContentTitle(mContext.getString(R.string.notification_title)) // TODO: Add better text to notification
                .setContentText(mContext.getString(R.string.notification_text))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.mContext);
        notificationManager.notify(10000, mBuilder.build());
    }

    /**
     * Get current time
     * Inspired by https://stackoverflow.com/a/834172
     * @return
     */
    private String getCurrentTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(cal.getTime());
    }
}
