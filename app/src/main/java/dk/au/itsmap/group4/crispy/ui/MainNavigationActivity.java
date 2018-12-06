package dk.au.itsmap.group4.crispy.ui;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.service.notifications.NotificationAlarm;

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


    public void changeToolbar(){
        setContentView(R.layout.recipe_detail_fragment);
        Toolbar t = findViewById(R.id.detail_toolbar);
        setSupportActionBar(t);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Random Title");
    }

    public void setToolbar(Toolbar t){
        mToolbar.setVisibility(View.GONE);
        setSupportActionBar(t);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Random Title");
    }

    public void setMainToolbarWithNavigation(String title){
        mToolbar.setVisibility(View.VISIBLE);
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    public void setMainToolbar(String title){
        mToolbar.setVisibility(View.VISIBLE);
        mToolbar.setTitle(title);
        setSupportActionBar(mToolbar);
    }
}
