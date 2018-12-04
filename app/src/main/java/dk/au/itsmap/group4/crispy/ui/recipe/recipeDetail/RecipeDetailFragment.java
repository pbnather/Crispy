package dk.au.itsmap.group4.crispy.ui.recipe.recipeDetail;

import android.app.Activity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import androidx.lifecycle.ViewModelProviders;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IIngredient;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.ui.recipe.recipeList.RecipeListActivity;
import dk.au.itsmap.group4.crispy.ui.recipe.RecipeViewModel;

public class RecipeDetailFragment extends Fragment {

    private RecipeViewModel mModel;
    private TableLayout ingredientsTable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);
        Activity activity = this.getActivity();
        ingredientsTable = rootView.findViewById(R.id.ingredientsTable);

        mModel.getSelectedRecipe().observe(this, (recipe) -> updateView(activity, rootView, recipe));
        mModel.getIngredientsForSelectedRecipe().observe(this, ingredients -> {

            ingredientsTable.removeAllViews();
            for(IIngredient ingredient : ingredients) {
                // add array of views
                addIngredientRow(inflater, container, ingredient);
            }
        });


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

       // ((TextView) rootView.findViewById(R.id.recipe_detail)).setText(recipe.getDescription());
        ((TextView) rootView.findViewById(R.id.recipe_description)).setText(recipe.getDescription());
    }

    private void addIngredientRow(LayoutInflater inflater, ViewGroup container, IIngredient ingredient){
        View ingredientRowLayout = inflater.inflate(R.layout.ingredient_row, container, false);

        TextView ingredientName = ingredientRowLayout.findViewById(R.id.ingredientNameText);
        TextView ingredientQuantity = ingredientRowLayout.findViewById(R.id.quantityText);
        TextView ingredientUnit = ingredientRowLayout.findViewById(R.id.unitText);

        if (ingredient != null) {
            ingredientName.setText(ingredient.getName());
            ingredientQuantity.setText(String.valueOf(ingredient.getQuantity()));
            ingredientUnit.setText(ingredient.getUnit());
            ingredientsTable.addView(ingredientRowLayout);
        }
    }
}
