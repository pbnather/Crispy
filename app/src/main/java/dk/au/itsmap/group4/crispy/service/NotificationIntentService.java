package dk.au.itsmap.group4.crispy.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import dk.au.itsmap.group4.crispy.database.FSRepository;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.model.IRepository;

public class NotificationIntentService extends IntentService {

    NotificationHelper mNotificationHelper;

    public NotificationIntentService() {
        super("NotificationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {

        mNotificationHelper = new NotificationHelper(this);

        (new NotifyTodaysMealAsyncTask()).execute(mNotificationHelper);

    }

    public static class NotifyTodaysMealAsyncTask extends AsyncTask<NotificationHelper, Void, Void> {
        @Override
        protected Void doInBackground(NotificationHelper... helpers) {
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