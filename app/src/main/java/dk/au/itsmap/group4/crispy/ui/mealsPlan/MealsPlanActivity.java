package dk.au.itsmap.group4.crispy.ui.mealsPlan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.ui.mealsPlan.MealsPlanViewModel;
import dk.au.itsmap.group4.crispy.ui.mealsPlan.addPlannedMeal.AddPlannedMealFragment;

public class MealsPlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meals_plan_activity);
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.container, new MealsPlanFragment())
//                    .commitNow();
//        }
    }

//    public void showAddToPlan() {
//        getSupportFragmentManager()
//                .beginTransaction()
//                .addToBackStack("mealsPlan")
//                .replace(R.id.container, new AddPlannedMealFragment())
//                .commit();
//    }
//
//    public void goBackToList() {
//        getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.container, new MealsPlanFragment())
//                .commit();
//    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.navigation_fragment).navigateUp();
    }



}
