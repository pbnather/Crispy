package dk.au.itsmap.group4.crispy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.database.entity.Recipe;
import dk.au.itsmap.group4.crispy.viewmodel.RecipeViewModel;


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
        mModel.getRecipes().observe(this, (recipeList) -> {
            if(mAdapter != null) {
                // todo: mAdapter.setData(recipeList);
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

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
    public void onRecipeClicked(Recipe recipe) {
        mModel.setSelectedRecipe(recipe);
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            // todo: arguments.putString(ARG_RECIPE_ID, recipe.getId());
            RecipeDetailFragment fragment = new RecipeDetailFragment();
            fragment.setArguments(arguments);
            this.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, RecipeDetailActivity.class);
            intent.putExtra("RECIPE_ID", recipe.getId());
            this.startActivity(intent);
        }
    }
}
