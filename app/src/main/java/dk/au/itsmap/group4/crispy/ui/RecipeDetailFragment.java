package dk.au.itsmap.group4.crispy.ui;

import android.app.Activity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.viewmodel.RecipeDetailViewModel;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {

    private RecipeDetailViewModel mModel;

    public RecipeDetailFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = ViewModelProviders.of(getActivity()).get(RecipeDetailViewModel.class);
        mModel.getRecipes().observe(getActivity(), recipes -> {
            Toast.makeText(getActivity(), "RECIPES_SIZE: " + recipes.size(), Toast.LENGTH_SHORT).show();
            Log.i("TOAST", "RECIPES_SIZE: " + recipes.size());
            String id = recipes.get(1).getId();
            mModel.getIngredientsForRecipeById(id).observe(getActivity(), ingredients -> {
                ingredients.remove(0);
                mModel.deleteIngredientsForRecipeById(id, ingredients);
            });
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);

        Activity activity = this.getActivity();
        CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle("Test Title");
        }

        ((TextView) rootView.findViewById(R.id.recipe_detail)).setText("test \n test \n test  \n test" +
                "test \n test \n test  \n test \n" +
                "test \n test \n test  \n test \n" +
                "test \n test \n test  \n test \n");
        ((TextView) rootView.findViewById(R.id.recipe_description)).setText("ASFFSAFSAFaFSAFASFASFASF");
        return rootView;
    }
}
