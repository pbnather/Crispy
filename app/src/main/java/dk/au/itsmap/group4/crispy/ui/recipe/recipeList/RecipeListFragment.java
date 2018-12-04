package dk.au.itsmap.group4.crispy.ui.recipe.recipeList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.ui.recipe.RecipeViewModel;

public class RecipeListFragment extends Fragment implements RecipesRecyclerViewAdapter.OnRecipeClickListener {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private Activity mActivity;
    private View mView;

    private RecipeViewModel mModel;

    private RecipesRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = ViewModelProviders.of(this).get(RecipeViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.recipe_list_fragment, container, false);
        mActivity = this.getActivity();

        Toolbar toolbar = (Toolbar) mActivity.findViewById(R.id.mainToolbar);
        toolbar.setTitle(R.string.recipies);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);


        FloatingActionButton fab = (FloatingActionButton) mView.findViewById(R.id.addRecipeButton);
        fab.setOnClickListener(view -> {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null)
                    .show();
        });

        if (mView.findViewById(R.id.recipe_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        // setup list adapters
        setupRecyclerView();

        // observe for model changes
        mModel.getAllRecipes().observe(this, (recipeList) -> {
            if(mAdapter != null) {
                mAdapter.setData(recipeList);
            }
        });

        return mView;

    }

    private void setupRecyclerView() {
        mRecyclerView = mView.findViewById(R.id.recipe_list);
        assert mRecyclerView != null;

        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecipesRecyclerViewAdapter(getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onRecipeClicked(IRecipe recipe) {

        mModel.selectRecipe(recipe.getId());

//        if (mTwoPane) {
//            // show only fragment
//            // TODO: check, if the fragment has to be replaced
//            RecipeDetailFragment fragment = new RecipeDetailFragment();
//            this.getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.recipe_detail_container, fragment)
//                    .commit();
//        } else {
//            // go to another activity
//            Intent intent = new Intent(this, RecipeDetailActivity.class);
//            intent.putExtra(EXTRA_RECIPE_ID, recipe.getId());
//            this.startActivity(intent);
//        }
    }

}
