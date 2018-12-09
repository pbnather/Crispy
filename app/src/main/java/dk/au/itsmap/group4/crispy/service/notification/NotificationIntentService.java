package dk.au.itsmap.group4.crispy.service.notification;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dk.au.itsmap.group4.crispy.database.FSRepository;
import dk.au.itsmap.group4.crispy.database.entity.Entity;
import dk.au.itsmap.group4.crispy.database.entity.Meal;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.model.IRepository;

public class NotificationIntentService extends IntentService {

    NotificationFactory mNotificationHelper;
    private IRepository mRepository;

    public NotificationIntentService() {
        super("NotificationIntentService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {

        mNotificationHelper = new NotificationFactory(this);

        mRepository = FSRepository.getInstance();

        Log.d("NOTIFICATION", "Alarm clock fired");

        // prepare listener for Firebase result
        OnCompleteListener<QuerySnapshot> listener = task -> {
            if(task.isSuccessful()) {
                QuerySnapshot snapshot = task.getResult();
                if(snapshot != null) {
                    List<IMeal> iMeals = new ArrayList<>();
                    for (QueryDocumentSnapshot doc : snapshot) {
                        IMeal fetchedDocument = doc.toObject(Meal.class);
                        if(Entity.class.isAssignableFrom(Meal.class))
                            ((Entity) fetchedDocument).setId(doc.getId());
                        iMeals.add(fetchedDocument);
                    }
                    if(iMeals.size() > 0) {
                        mNotificationHelper.notifyAboutTodayMeals(iMeals);
                    }
                }
            }
        };

        // prepare tomorrow's midnight
        Date date = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.add(Calendar.DATE, 1);

        //
        mRepository.getMealsInRangeOnce(new Date(), c.getTime(), listener);

    }

}