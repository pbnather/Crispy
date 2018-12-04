package dk.au.itsmap.group4.crispy.ui;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.service.notifications.NotificationAlarm;

public class MainNavigationActivity extends CrispyAuthenticatedActivity {

    Toolbar mToolbar;

    NavController mNavController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_navigation_activity);

        mNavController = Navigation.findNavController(this, R.id.navigation_fragment);

        mToolbar = findViewById(R.id.mainToolbar);
        mToolbar.setTitle(R.string.crispy_planner);
        setSupportActionBar(mToolbar);

        NotificationAlarm.startAlarm(this);
    }

    @Override
    public NavController getNavController() {
        return mNavController;
    }

    @Override
    public boolean onSupportNavigateUp() {
        return mNavController.navigateUp();
    }


}
