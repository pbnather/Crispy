package dk.au.itsmap.group4.crispy.ui.groceryList;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import dk.au.itsmap.group4.crispy.R;

public class GroceryListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grocery_list_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, GroceryListFragment.newInstance())
                    .commitNow();
        }
    }
}
