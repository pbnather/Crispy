package dk.au.itsmap.group4.crispy.ui.recipe.recipeDetail;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IIngredient;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.ui.recipe.RecipeViewModel;

public class RecipeEditFragment extends Fragment {

    private RecipeViewModel mModel;
    private IngredientAutoCompleteAdapter mAutoAdapter;

    private TableLayout ingredientsTable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_edit, container, false);

        Activity activity = getActivity();
        ingredientsTable = rootView.findViewById(R.id.ingredientsTable);

        mModel.getSelectedRecipe().observe(this, recipe -> updateView(activity, rootView, recipe));
        mModel.getIngredientsForSelectedRecipe().observe(this, ingredients -> {
            ingredientsTable.removeAllViews();
            for(IIngredient ingredient : ingredients) {
                View ingredientRowLayout = inflater.inflate(R.layout.edit_ingredient_row, container, false);
                AutoCompleteTextView ingredientName = ingredientRowLayout.findViewById(R.id.ingredientNameEditText);
                EditText ingredientQuantity = ingredientRowLayout.findViewById(R.id.quantityEditText);
                Spinner unitPicker = ingredientRowLayout.findViewById(R.id.unitPicker);
                Button deleteRowBtn = ingredientRowLayout.findViewById(R.id.deleteRowBtn);

                ingredientName.setText(ingredient.getName());
                ingredientQuantity.setText(String.valueOf(ingredient.getQuantity()));
                ingredientsTable.addView(ingredientRowLayout);
            }
        });

//        mAutoAdapter = new IngredientAutoCompleteAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line);
//        autoCompleteIngredient.setAdapter(mAutoAdapter);

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

        ((EditText) rootView.findViewById(R.id.recipe_description)).setText(recipe.getDescription());
        ((EditText) rootView.findViewById(R.id.recipe_description)).setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        ((EditText) rootView.findViewById(R.id.recipe_description)).setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        ((EditText) rootView.findViewById(R.id.recipe_description)).setMovementMethod(new ScrollingMovementMethod());
    }

}