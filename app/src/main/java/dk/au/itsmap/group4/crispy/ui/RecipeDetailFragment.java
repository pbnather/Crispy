package dk.au.itsmap.group4.crispy.ui;

import android.app.Activity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.database.entity.Recipe;
import dk.au.itsmap.group4.crispy.viewmodel.RecipeViewModel;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {

    public static final String ARG_RECIPE_ID = "ARG_RECIPE_ID";

    private Recipe mRecipe;
    private RecipeViewModel mModel;

    private int mRecipeId;

    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);

        mRecipeId = getArguments().getInt(ARG_RECIPE_ID, 0);


    }

    private void updateView(Activity activity, View rootView, Recipe recipe) {
        if(activity != null) {
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(recipe.getTitle());
            }
        }

        ((TextView) rootView.findViewById(R.id.recipe_detail)).setText(recipe.getDescription());
        ((TextView) rootView.findViewById(R.id.recipe_description)).setText(recipe.getDescription());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);

        Activity activity = this.getActivity();

        mModel.getRecipe(mRecipeId).observe(this, (recipe) -> {
            updateView(activity, rootView, recipe);
        });

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
