package dk.au.itsmap.group4.crispy.ui.mealsPlan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.Navigation;
import dk.au.itsmap.group4.crispy.R;

public class MealsPlanActivity extends AppCompatActivity {

    Toolbar mToolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meals_plan_activity);


        mToolbar = (Toolbar) findViewById(R.id.mainToolbar);
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

    //From: https://developer.android.com/training/appbar/actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnGroceryList:
               //TODO: navigate to grocery list
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return Navigation.findNavController(this, R.id.navigation_fragment).navigateUp();
    }



}
