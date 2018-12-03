package dk.au.itsmap.group4.crispy.service.notifications;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import dk.au.itsmap.group4.crispy.database.FSRepository;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.model.IRepository;

public class NotificationIntentService extends IntentService {

    NotificationFactory mNotificationHelper;

    public NotificationIntentService() {
        super("NotificationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {

        mNotificationHelper = new NotificationFactory(this);

        Log.d("NOTIFICATION", "Alarm clock fired");

        mNotificationHelper.notifyAboutTodaysMeals();

        IRepository repository = FSRepository.getInstance();
        List<IMeal> meals = repository.getAllMeals().getValue();
        // todo: why is meals empty?

//        if(meals == null) {
//            return null;
//        }
//
//        if(meals.size() == 0) {
//            // no upcoming meals
//            // TODO: send notification - create new meal for today!
//        } else {
//            IMeal nextMeal = meals.get(0);
//            // TODO: send notification - this is yours meal for today!
//        }

        //  (new NotifyTodaysMealAsyncTask()).execute(mNotificationHelper);


    }


    // there is apparently no need to have async task in intentService
    public static class NotifyTodaysMealAsyncTask extends AsyncTask<NotificationFactory, Void, Void> {
        @Override
        protected Void doInBackground(NotificationFactory... helpers) {
            // todo
            Log.d("NOTIFICATION", "Alarm clock fired");

            helpers[0].notifyAboutTodaysMeals();

            IRepository repository = FSRepository.getInstance();
            List<IMeal> meals = repository.getAllMeals().getValue();

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