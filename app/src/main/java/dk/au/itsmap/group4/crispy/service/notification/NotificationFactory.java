package dk.au.itsmap.group4.crispy.service.notification;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.format.DateUtils;

import java.util.List;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.ui.MainNavigationActivity;

/**
 * Class for creating notifications
 */
class NotificationFactory
{
    private static final String CHANNEL_ID_STR = "crispy_app_channel";
    private static final int CHANNEL_ID_INT = 100000;
    private Context mContext;

    NotificationFactory(Context context) {

        mContext = context;

        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_STR, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

    }

    /**
     * Creates notification about today's meal
     */
    void notifyAboutTodayMeals(List<IMeal> meals) {
        Intent intent = new Intent(mContext, MainNavigationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.mContext, 0, intent, 0);

        String text = "";
        if(meals.size() > 0) {
            text += meals.get(0).getTitle() + " " + mContext.getString(R.string.at) + " " + DateUtils.formatDateTime(mContext, meals.get(0).getDate().getTime(), DateUtils.FORMAT_SHOW_TIME);
        }
        if(meals.size() > 1) {
            text += " + " + (meals.size()-1) + " " + mContext.getString(R.string.more);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(mContext, CHANNEL_ID_STR)
                .setSmallIcon(R.drawable.ic_pot)
                .setContentTitle(mContext.getString(R.string.notification_title))
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);
        notificationManager.notify(CHANNEL_ID_INT, mBuilder.build());
    }

}
