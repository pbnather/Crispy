package dk.au.itsmap.group4.crispy.ui.mealsPlan;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

}
