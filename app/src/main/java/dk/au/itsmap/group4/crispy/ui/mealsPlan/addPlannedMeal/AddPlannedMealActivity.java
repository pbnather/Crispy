package dk.au.itsmap.group4.crispy.ui.mealsPlan.addPlannedMeal;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import dk.au.itsmap.group4.crispy.R;

public class AddPlannedMealActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_planned_meal_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, AddPlannedMealFragment.newInstance())
                    .commitNow();
        }
    }
}
