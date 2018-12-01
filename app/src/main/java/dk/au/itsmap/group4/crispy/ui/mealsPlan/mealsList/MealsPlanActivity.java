package dk.au.itsmap.group4.crispy.ui.mealsPlan.mealsList;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import dk.au.itsmap.group4.crispy.R;

public class MealsPlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meals_plan_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new MealsPlanFragment())
                    .commitNow();
        }
    }


}
