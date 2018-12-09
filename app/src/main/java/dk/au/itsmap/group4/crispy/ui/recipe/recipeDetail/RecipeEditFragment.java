package dk.au.itsmap.group4.crispy.ui.recipe.recipeDetail;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.database.entity.Ingredient;
import dk.au.itsmap.group4.crispy.database.entity.Recipe;
import dk.au.itsmap.group4.crispy.model.IIngredient;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.ui.MainNavigationActivity;
import dk.au.itsmap.group4.crispy.ui.recipe.RecipeViewModel;
import dk.au.itsmap.group4.crispy.utils.GlideApp;

public class RecipeEditFragment extends Fragment {

    private RecipeViewModel mModel;
    private AutoCompleteIngredientAdapter mAutoAdapter;
    private Button btnAddIngredient, btnDeleteRecipe;
    private TableLayout ingredientsTable;
    private List<IIngredient> added, mIngredients;
    private List<IIngredient> deleted;
    private ArrayAdapter mUnitSpinnerAdapter;
    private MainNavigationActivity mActivity;
    private View mView, mInsideView;
    private View.OnClickListener deleteRowListener;
    private EditText mDescriptionEdit, mTitleEdit;
    private IRecipe mRecipe;
    private ImageView mRecipeToolbarImage;
    private FloatingActionButton btnSaveRecipe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem account = menu.findItem(R.id.btnAccount);
        MenuItem list = menu.findItem(R.id.btnGroceryList);
        account.setVisible(false);
        list.setVisible(false);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mActivity = (MainNavigationActivity) getActivity();


        // inflate inner layout to scroll view
        mView = inflater.inflate(R.layout.recipe_detail_fragment, container, false);
        mInsideView = inflater.inflate(R.layout.recipe_detail_inner_edit, mView.findViewById(R.id.recipe_detail_container), true);

        Toolbar superToolbar = mView.findViewById(R.id.detail_toolbar);
        mRecipeToolbarImage = mView.findViewById(R.id.recipeImageToolbar);
        if(superToolbar != null) {
            superToolbar.setTitle(getText(R.string.edit_recipe));
            mActivity.setToolbar(superToolbar);
        }

       // mActivity.setMainToolbarWithNavigation(getText(R.string.edit_recipe).toString());


        added = new ArrayList<>();
        deleted = new ArrayList<>();

        ingredientsTable = mView.findViewById(R.id.ingredientsTable);
        btnAddIngredient = mView.findViewById(R.id.btnAddIngredient);
        btnDeleteRecipe = mView.findViewById(R.id.btnDelete);

        btnSaveRecipe = mView.findViewById(R.id.btnEditRecipe);
        btnSaveRecipe.setImageDrawable(getResources().getDrawable(R.drawable.save));

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


        btnDeleteRecipe.setOnClickListener(v -> deleteRecipe());

        btnSaveRecipe.setOnClickListener(v -> saveRecipe());

        btnAddIngredient.setOnClickListener(v -> addIngredientRow(inflater, container, null));

        mAutoAdapter = new AutoCompleteIngredientAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line);

        mModel.getMode().observe(this, mode -> {
            if(mode == RecipeViewModel.Mode.ADD) {
                mRecipe = new Recipe();
                updateView(mRecipe);
            }
            if(mode == RecipeViewModel.Mode.EDIT) {
                mModel.getSelectedRecipe().observe(this, recipe -> {
                    mRecipe = recipe;
                    updateView(mRecipe);
                });
            }
        });



        mModel.getIngredientsForSelectedRecipe().observe(this, iIngredients -> {

            mIngredients = iIngredients;
            ingredientsTable.removeAllViews();
            if(iIngredients == null) {
                return;
            }
            for(IIngredient ingredient : iIngredients) {
                // add array of views
                addIngredientRow(inflater, container, ingredient);
            }
        });

        mTitleEdit = mView.findViewById(R.id.recipe_title);
        setDescription();

        return mView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mModel.getSelectedRecipe().removeObservers(this);
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


    private void setDescription(){
        mDescriptionEdit = mView.findViewById(R.id.recipe_description);
        mDescriptionEdit.setInputType(
                InputType.TYPE_CLASS_TEXT |
                InputType.TYPE_TEXT_FLAG_MULTI_LINE |
                InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        );
        mDescriptionEdit.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI);
        mDescriptionEdit.setMovementMethod(new ScrollingMovementMethod());
    }
    private void updateView(IRecipe recipe) {
        CollapsingToolbarLayout appBarLayout = mView.findViewById(R.id.toolbar_layout);
        if(appBarLayout != null) {
            appBarLayout.setTitle(getText(R.string.edit_recipe));
        }
        if(recipe == null) {
            ((EditText) mView.findViewById(R.id.recipe_title)).setText("");
            ((EditText) mView.findViewById(R.id.recipe_description)).setText("");
            btnDeleteRecipe.setVisibility(View.GONE);
            return;
        }
        if(mView != null) {
            if(mRecipeToolbarImage != null) {
                GlideApp.with(mView)
                        .load(recipe.getImage_url())
                        .centerCrop()
                        .into(mRecipeToolbarImage);
            }
            ((EditText) mView.findViewById(R.id.recipe_title)).setText(recipe.getTitle());
            ((EditText) mView.findViewById(R.id.recipe_description)).setText(recipe.getDescription());
            btnDeleteRecipe.setVisibility(View.VISIBLE);
        }


        ((EditText) mView.findViewById(R.id.recipe_title)).setText(recipe.getTitle());
        ((EditText) mView.findViewById(R.id.recipe_description)).setText(recipe.getDescription());
        if(mModel.getMode().getValue() != RecipeViewModel.Mode.ADD) {
            btnDeleteRecipe.setVisibility(View.VISIBLE);
        }

    }

    private boolean saveIngredients(){
        for(int i=0, j=ingredientsTable.getChildCount(); i<j; i++){
                View ingredientRowLayout = (View) ingredientsTable.getChildAt(i);
                Ingredient ing = getIngredient(ingredientRowLayout);
                if (ing != null)
                    added.add(ing);
                else {
                    Toast.makeText(getActivity(), R.string.empty_ingredient_msg, Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
        return true;
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

    private void saveRecipe(){
        String description = mDescriptionEdit.getText().toString();
        String title = mTitleEdit.getText().toString();
        String id = null;
        String image = null;
        if (title.isEmpty()) {
            Toast.makeText(getActivity(), R.string.empty_title_msg, Toast.LENGTH_LONG).show();
            return;
        }
        if (mRecipe != null) {
            id = mRecipe.getId();
            image = mRecipe.getImage_url();
        }
        Recipe updatedRecipe = new Recipe(id, title , image, description);
        if (saveIngredients()){
            mModel.saveRecipe(updatedRecipe, added, deleted);
            added.clear();
            deleted.clear();
            Toast.makeText(getActivity(), R.string.recipe_saved_msg, Toast.LENGTH_LONG).show();
            if(mModel.isSinglePage()) {
                // mModel.selectRecipe(updatedRecipe);
                mModel.setMode(RecipeViewModel.Mode.VIEW);
//                Navigation.findNavController(mView).popBackStack();
//                mActivity.getNavController().navigate(R.id.recipeListFragment);
            } else {
                Navigation.findNavController(mView).navigateUp();
            }
        }
    }

    private void deleteRecipe(){
        mModel.deleteRecipe((Recipe) mRecipe, mIngredients);
        Toast.makeText(getActivity(), R.string.recipe_deleted_msg, Toast.LENGTH_LONG).show();
        // go twice up
        if(mModel.isSinglePage()) {
            mModel.selectRecipe(null);
            mModel.setMode(RecipeViewModel.Mode.LIST);
//            Navigation.findNavController(mView).popBackStack();
//            mActivity.getNavController().navigate(R.id.recipeListFragment);
        } else {
            Navigation.findNavController(mView).popBackStack();
            Navigation.findNavController(mView).navigateUp();
        }

    }
}