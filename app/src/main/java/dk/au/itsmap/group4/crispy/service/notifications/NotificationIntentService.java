package dk.au.itsmap.group4.crispy.service.notifications;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.util.concurrent.CountDownLatch;

public class NotificationIntentService extends IntentService {

    private CountDownLatch doneSignal = new CountDownLatch(1);

    NotificationFactory mNotificationHelper;

    public NotificationIntentService() {
        super("NotificationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {

        mNotificationHelper = new NotificationFactory(this);

        (new NotifyTodaysMealAsyncTask()).execute(mNotificationHelper);

        try {
            doneSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public static class NotifyTodaysMealAsyncTask extends AsyncTask<NotificationFactory, Void, Void> {
        @Override
        protected Void doInBackground(NotificationFactory... helpers) {
            // todo
            Log.d("NOTIFICATION", "Alarm clock fired");

            helpers[0].notifyAboutTodaysMeals();

            return null;
            // todo: check why there are no values in the repository

//            IRepository repository = FSRepository.getInstance();
//            List<IMeal> meals = repository.getAllMeals().getValue();
//
//            if(meals == null) {
//                return null;
//            }
//
//            if(meals.size() == 0) {
//                // no upcoming meals
//                // TODO: send notification
//            } else {
//                IMeal nextMeal = meals.get(0);
//                // TODO: send notification
//            }
//
//            return null;
        }
    }

}