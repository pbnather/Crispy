package dk.au.itsmap.group4.crispy.ui.recipe.recipeDetail;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.ui.recipe.RecipeViewModel;

public class RecipeEditFragment extends Fragment {

    private RecipeViewModel mModel;
    private IngredientAutoCompleteAdapter mAutoAdapter;
    private AutoCompleteTextView autoCompleteIngredient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_edit, container, false);

        Activity activity = this.getActivity();
        autoCompleteIngredient = rootView.findViewById(R.id.recipe_detail);

        mModel.getSelectedRecipe().observe(this, (recipe) -> updateView(activity, rootView, recipe));

        mAutoAdapter = new IngredientAutoCompleteAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line);
        autoCompleteIngredient.setAdapter(mAutoAdapter);

        return rootView;
    }

    /**
     * Update View of this fragment
     *
     * @param activity Base Activity of this fragment
     * @param rootView View of this fragment
     * @param recipe   IRecipe to be shown
     */
    private void updateView(Activity activity, View rootView, IRecipe recipe) {
        if (recipe == null) {
            return;
        }
        if (activity != null) {
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(recipe.getTitle());
            }
        }

        ((TextView) rootView.findViewById(R.id.recipe_detail)).setText(recipe.getDescription());
        ((TextView) rootView.findViewById(R.id.recipe_description)).setText("JOKE");
    }

}