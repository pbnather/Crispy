package dk.au.itsmap.group4.crispy.ui.mealsPlan;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.ui.CrispyAuthenticatedActivity;
import dk.au.itsmap.group4.crispy.service.notifications.NotificationAlarm;

public class MealsPlanActivity extends CrispyAuthenticatedActivity {

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meals_plan_activity);

        mToolbar = findViewById(R.id.mainToolbar);
        mToolbar.setTitle(R.string.crispy_planner);
        setSupportActionBar(mToolbar);

        NotificationAlarm.startAlarm(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.navigation_fragment).navigateUp();
    }
}
