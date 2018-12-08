package dk.au.itsmap.group4.crispy.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.service.notification.NotificationAlarm;

public class MainNavigationActivity extends AuthActivity {

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

        // register notification alarm
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

    @Override
    public void setToolbar(Toolbar t){
        mToolbar.setVisibility(View.GONE);
        setSupportActionBar(t);
        if(t == null) {
            return;
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Random Title");
    }

    @Override
    public void setMainToolbarWithNavigation(String title) {
        mToolbar = findViewById(R.id.mainToolbar);
        mToolbar.setVisibility(View.VISIBLE);
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void setMainToolbar(String title){
        mToolbar = findViewById(R.id.mainToolbar);
        mToolbar.setVisibility(View.VISIBLE);
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean isOrientationLandscape() {
        return getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}
