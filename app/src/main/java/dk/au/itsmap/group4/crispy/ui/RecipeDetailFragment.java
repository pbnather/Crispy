package dk.au.itsmap.group4.crispy.ui;

import android.app.Activity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.Repository;
import dk.au.itsmap.group4.crispy.database.entity.Recipe;
import dk.au.itsmap.group4.crispy.viewmodel.RecipeDetailViewModel;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {

    private Recipe mRecipe;
    private RecipeDetailViewModel mModel;

    public RecipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Observer<Recipe> recipeObserver = recipe -> {
//            TODO: Update UI on Recipe onchange() method
        };

        mModel = ViewModelProviders.of(getActivity()).get(RecipeDetailViewModel.class);
//        TODO: Add MockRepository and send Recipe.id in the Intent
//        mModel.getRecipe(1).observe(this, recipeObserver);
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
