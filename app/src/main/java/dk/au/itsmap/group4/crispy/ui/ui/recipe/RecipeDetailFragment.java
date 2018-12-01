package dk.au.itsmap.group4.crispy.ui.ui.recipe;

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
import dk.au.itsmap.group4.crispy.model.IRecipe;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {

    private RecipeViewModel mModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);

        Activity activity = this.getActivity();

        mModel.getSelectedRecipe().observe(this, (recipe) -> updateView(activity, rootView, recipe));

        return rootView;
    }

    /**
     * Update View of this fragment
     * @param activity Base Activity of this fragment
     * @param rootView View of this fragment
     * @param recipe IRecipe to be shown
     */
    private void updateView(Activity activity, View rootView, IRecipe recipe) {
        if(recipe == null) {
            return;
        }
        if(activity != null) {
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(recipe.getTitle());
            }
        }

        ((TextView) rootView.findViewById(R.id.recipe_detail)).setText(recipe.getDescription());
        ((TextView) rootView.findViewById(R.id.recipe_description)).setText(recipe.getDescription());
    }
}
