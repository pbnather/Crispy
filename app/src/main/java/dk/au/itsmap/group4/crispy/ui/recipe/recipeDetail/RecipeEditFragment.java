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
import android.widget.TableRow;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.database.entity.Ingredient;
import dk.au.itsmap.group4.crispy.model.IIngredient;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.ui.recipe.RecipeViewModel;

public class RecipeEditFragment extends Fragment {

    private RecipeViewModel mModel;
    private IngredientAutoCompleteAdapter mAutoAdapter;
    private Button btnAddIngredient, banDeleteRecipe;
    private TableLayout ingredientsTable;
    private IRecipe mRecipe;
    private List<IIngredient> added;
    private List<IIngredient> deleted;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        added = new ArrayList<>();
        deleted = new ArrayList<>();
        View rootView = inflater.inflate(R.layout.recipe_edit, container, false);

        Activity activity = getActivity();
        ingredientsTable = rootView.findViewById(R.id.ingredientsTable);
        btnAddIngredient = rootView.findViewById(R.id.btnAddIngredient);
        banDeleteRecipe = rootView.findViewById(R.id.btnDeleteRecipy);

        banDeleteRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveIngredients();
                mModel.saveRecipe(mModel.getSelectedRecipe().getValue(), added, deleted);
                added.clear();
                deleted.clear();
            }
        });

        View.OnClickListener deleteRowListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View tr = (View) v.getParent();
                Ingredient ing = getIngredient(tr);
                if (ing != null)
                    deleted.add(ing);
                ingredientsTable.removeView(tr);
            }
        };

        ArrayList<View> addedIngredients = new ArrayList<View>();

        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View newIngredient = inflater.inflate(R.layout.edit_ingredient_row, container, false);
                newIngredient.setTag(null);
                AutoCompleteTextView ingredientName = newIngredient.findViewById(R.id.ingredientNameEditText);
                ingredientName.setAdapter(mAutoAdapter);
                Button deleteRowBtn = newIngredient.findViewById(R.id.deleteRowBtn);
                deleteRowBtn.setOnClickListener(deleteRowListener);
                addedIngredients.add(newIngredient);
                ingredientsTable.addView(newIngredient);
            }
        });

        mAutoAdapter = new IngredientAutoCompleteAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line);

        mModel.getSelectedRecipe().observe(this, recipe -> updateView(activity, rootView, recipe));

        mModel.getIngredientsForSelectedRecipe().observe(this, ingredients -> {

            ingredientsTable.removeAllViews();

            for(IIngredient ingredient : ingredients) {
                // add array of views
                View ingredientRowLayout = inflater.inflate(R.layout.edit_ingredient_row, container, false);
                AutoCompleteTextView ingredientName = ingredientRowLayout.findViewById(R.id.ingredientNameEditText);
                EditText ingredientQuantity = ingredientRowLayout.findViewById(R.id.quantityEditText);
                Spinner unitPicker = ingredientRowLayout.findViewById(R.id.unitPicker);
                Button deleteRowBtn = ingredientRowLayout.findViewById(R.id.deleteRowBtn);
                deleteRowBtn.setOnClickListener(deleteRowListener);
                ingredientName.setAdapter(mAutoAdapter);
                ingredientRowLayout.setTag(ingredient.getId());
                ingredientName.setText(ingredient.getName());
                ingredientQuantity.setText(String.valueOf(ingredient.getQuantity()));
                ingredientsTable.addView(ingredientRowLayout);
            }
        });



        ((EditText) rootView.findViewById(R.id.recipe_description)).setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        ((EditText) rootView.findViewById(R.id.recipe_description)).setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        ((EditText) rootView.findViewById(R.id.recipe_description)).setMovementMethod(new ScrollingMovementMethod());

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
    }

    private void saveIngredients(){
        for(int i=0, j=ingredientsTable.getChildCount(); i<j; i++){
                View ingredientRowLayout = (View) ingredientsTable.getChildAt(i);
                Ingredient ing = getIngredient(ingredientRowLayout);
                if (ing != null)
                    added.add(ing);
                else
                    Toast.makeText( getActivity(), "NULL", Toast.LENGTH_SHORT).show();
                //TODO:Change message
            }
    }

    private Ingredient getIngredient(View tr){
        AutoCompleteTextView ingredientName = tr.findViewById(R.id.ingredientNameEditText);
        EditText ingredientQuantity = tr.findViewById(R.id.quantityEditText);
        Spinner unitPicker = tr.findViewById(R.id.unitPicker);
        String id = (String) tr.getTag();
        String name = ingredientName.getText().toString();
        String quantity = ingredientQuantity.getText().toString();
        if (name == null | name.isEmpty() | quantity.isEmpty()){

            return null;
        }
        else{
            Ingredient nIng = new Ingredient(id, name , "", Double.valueOf(quantity));
            return nIng;
        }
    }
}