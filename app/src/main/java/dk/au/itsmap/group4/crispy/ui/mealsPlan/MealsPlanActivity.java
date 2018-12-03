package dk.au.itsmap.group4.crispy.ui.mealsPlan;

import android.os.Bundle;
import android.view.Menu;


import androidx.appcompat.widget.Toolbar;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.ui.CrispyAuthenticatedActivity;

public class MealsPlanActivity extends CrispyAuthenticatedActivity {

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meals_plan_activity);

        mToolbar = findViewById(R.id.mainToolbar);
        mToolbar.setTitle(R.string.crispy_planner);
        setSupportActionBar(mToolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

}
