package dk.au.itsmap.group4.crispy.ui.recipe.recipeDetail;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.database.entity.Ingredient;
import dk.au.itsmap.group4.crispy.model.IIngredient;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.ui.recipe.RecipeViewModel;

public class RecipeEditFragment extends Fragment {

    private RecipeViewModel mModel;
    private IngredientAutoCompleteAdapter mAutoAdapter;
    private Button btnAddIngredient, btnDeleteRecipe;
    private TableLayout ingredientsTable;
    private List<IIngredient> added;
    private List<IIngredient> deleted;
    private ArrayAdapter mUnitSpinnerAdapter;
    private Activity mActivity;
    private View mView;
    private View.OnClickListener deleteRowListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        added = new ArrayList<>();
        deleted = new ArrayList<>();
        mView = inflater.inflate(R.layout.recipe_edit, container, false);
        mActivity = getActivity();

        ingredientsTable = mView.findViewById(R.id.ingredientsTable);
        btnAddIngredient = mView.findViewById(R.id.btnAddIngredient);
        btnDeleteRecipe = mView.findViewById(R.id.btnDeleteRecipy);

        deleteRowListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View tr = (View) v.getParent();
                Ingredient ing = getIngredient(tr);
                if (ing != null)
                    deleted.add(ing);
                ingredientsTable.removeView(tr);
            }
        };

        btnDeleteRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveIngredients();
                mModel.saveRecipe(mModel.getSelectedRecipe().getValue(), added, deleted);
                added.clear();
                deleted.clear();
            }
        });

        btnAddIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIngredientRow(inflater, container, null);
            }
        });

        mAutoAdapter = new IngredientAutoCompleteAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line);

        mModel.getSelectedRecipe().observe(this, recipe -> updateView(mActivity, mView, recipe));

        mModel.getIngredientsForSelectedRecipe().observe(this, ingredients -> {

            ingredientsTable.removeAllViews();

            for(IIngredient ingredient : ingredients) {
                // add array of views
                addIngredientRow(inflater, container, ingredient);
            }
        });

        fillEditFields();

        return mView;
    }

    private void addIngredientRow(LayoutInflater inflater, ViewGroup container, IIngredient ingredient){
        View ingredientRowLayout = inflater.inflate(R.layout.edit_ingredient_row, container, false);
        AutoCompleteTextView ingredientName = ingredientRowLayout.findViewById(R.id.ingredientNameEditText);
        EditText ingredientQuantity = ingredientRowLayout.findViewById(R.id.quantityEditText);
        Spinner unitPicker = ingredientRowLayout.findViewById(R.id.unitPicker);
        Button deleteRowBtn = ingredientRowLayout.findViewById(R.id.deleteRowBtn);

        deleteRowBtn.setOnClickListener(deleteRowListener);
        ingredientName.setAdapter(mAutoAdapter);
        setUnitSpinner(unitPicker);
        if (ingredient != null) {
            ingredientRowLayout.setTag(ingredient.getId());
            ingredientName.setText(ingredient.getName());
            ingredientQuantity.setText(String.valueOf(ingredient.getQuantity()));
            int unitNr = mModel.unitNr(ingredient.getUnit());
            unitPicker.setSelection(unitNr);
        }
        else
            ingredientRowLayout.setTag(null);
        ingredientsTable.addView(ingredientRowLayout);
    }


    private void fillEditFields(){
        ((EditText) mView.findViewById(R.id.recipe_description)).setInputType(InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        ((EditText) mView.findViewById(R.id.recipe_description)).setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        ((EditText) mView.findViewById(R.id.recipe_description)).setMovementMethod(new ScrollingMovementMethod());
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
        String unit = unitPicker.getSelectedItem().toString();
        if (name == null | name.isEmpty() | quantity.isEmpty() | unit.isEmpty()){
            return null;
        }
        else{
            Ingredient nIng = new Ingredient(id, name , unit , Double.valueOf(quantity));
            return nIng;
        }
    }

    private void setUnitSpinner(Spinner unitSpinner) {
        // assign to user
        mUnitSpinnerAdapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_spinner_dropdown_item, mModel.getPossibleUnits());
        unitSpinner.setAdapter(mUnitSpinnerAdapter);
        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //mMeal.setCookName(parent.getItemAtPosition(position).toString());
                //mModel.getSelectedMeal().setValue(mMeal);
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
}