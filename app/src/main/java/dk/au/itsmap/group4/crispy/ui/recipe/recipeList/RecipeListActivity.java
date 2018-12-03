package dk.au.itsmap.group4.crispy.ui.recipe.recipeList;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.ui.recipe.RecipeViewModel;
import dk.au.itsmap.group4.crispy.ui.recipe.recipeDetail.RecipeDetailActivity;
import dk.au.itsmap.group4.crispy.ui.recipe.recipeDetail.RecipeDetailFragment;


/**
 * An activity representing a list of Recipes. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends AppCompatActivity implements RecipesRecyclerViewAdapter.OnRecipeClickListener {

    public static final String EXTRA_RECIPE_ID = "EXTRA_RECIPE_ID";

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private RecipeViewModel mModel;

    private RecipesRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        if (findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        // get view model
        mModel = ViewModelProviders.of(this).get(RecipeViewModel.class);

        // setup list adapters
        mAdapter = new RecipesRecyclerViewAdapter(this, this);
        RecyclerView recyclerView = findViewById(R.id.recipe_list);
        assert recyclerView != null;
        recyclerView.setAdapter(mAdapter);

        // observe for model changes
        mModel.getAllRecipes().observe(this, (recipeList) -> {
            if(mAdapter != null) {
                mAdapter.setData(recipeList);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.mainToolbar);
        toolbar.setTitle(R.string.recipies);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onRecipeClicked(IRecipe recipe) {

        mModel.selectRecipe(recipe.getId());

        if (mTwoPane) {
            // show only fragment
            // TODO: check, if the fragment has to be replaced
            RecipeDetailFragment fragment = new RecipeDetailFragment();
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail_container, fragment)
                    .commit();
        } else {
            // go to another activity
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra(EXTRA_RECIPE_ID, recipe.getId());
            this.startActivity(intent);
        }
    }


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
}
