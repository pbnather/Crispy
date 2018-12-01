package dk.au.itsmap.group4.crispy.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.DialogFragment;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.ui.ui.addplannedmeal.AddPlannedMealFragment;
import dk.au.itsmap.group4.crispy.ui.ui.addplannedmeal.TimePickerFragment;

public class AddPlannedMeal extends AppCompatActivity {

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
