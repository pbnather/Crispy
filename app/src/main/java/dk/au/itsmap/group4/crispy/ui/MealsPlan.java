package dk.au.itsmap.group4.crispy.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.ui.ui.mealsplan.MealsPlanFragment;

public class MealsPlan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meals_plan_activity);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MealsPlanFragment.newInstance())
                    .commitNow();
        }
    }
}
