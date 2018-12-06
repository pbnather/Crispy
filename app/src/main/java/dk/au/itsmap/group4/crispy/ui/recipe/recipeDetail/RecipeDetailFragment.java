package dk.au.itsmap.group4.crispy.ui.recipe.recipeDetail;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IIngredient;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.service.GlideApp;
import dk.au.itsmap.group4.crispy.ui.MainNavigationActivity;
import dk.au.itsmap.group4.crispy.ui.recipe.RecipeViewModel;

public class RecipeDetailFragment extends Fragment {

    private RecipeViewModel mModel;
    private TableLayout ingredientsTable;
    private MainNavigationActivity mActivity;
    private View mView, insideView;
    private FloatingActionButton btnEditRecipe;
    private ImageView mRecipeToolbarImage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem account =menu.findItem(R.id.btnAccount);
        MenuItem list = menu.findItem(R.id.btnGroceryList);
        account.setVisible(false);
        list.setVisible(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (MainNavigationActivity) this.getActivity();

        mView = inflater.inflate(R.layout.recipe_detail_fragment, container, false);
        Toolbar superToolbar = mView.findViewById(R.id.detail_toolbar);
        mRecipeToolbarImage = mView.findViewById(R.id.recipeImageToolbar);

        mActivity.setToolbar(superToolbar);

        // inflate inner layout to scroll view
        insideView = inflater.inflate(R.layout.recipe_detail_inner_detail, mView.findViewById(R.id.recipe_detail_container));

//        mActivity.changeToolbar();
//        mView = inflater.inflate(R.layout.recipe_detail_inner_detail, container, false);

        btnEditRecipe = mView.findViewById(R.id.btnEditRecipe);
        btnEditRecipe.setOnClickListener(v -> {
            Navigation.findNavController(mView).navigate(R.id.recipeEditFragment);
        });

        ingredientsTable = mView.findViewById(R.id.ingredientsTable);

        mModel.getSelectedRecipe().observe(this, (recipe) -> updateView(recipe));

        mModel.getIngredientsForSelectedRecipe().observe(this, ingredients -> {

            ingredientsTable.removeAllViews();
            for(IIngredient ingredient : ingredients) {
                // add array of views
                addIngredientRow(inflater, container, ingredient);
            }
        });


        return mView;
    }

    /**
     * Update View of this fragment
     * @param recipe IRecipe to be shown
     */
    private void updateView(IRecipe recipe) {
        if(recipe == null) {
            return;
        }
        if(mActivity != null) {
            CollapsingToolbarLayout appBarLayout = mView.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(recipe.getTitle());
                GlideApp.with(mView)
                        .load(recipe.getImage_url())
                        .centerCrop()
                        .into(mRecipeToolbarImage);
            }
        }

       // ((TextView) rootView.findViewById(R.id.recipe_detail_inner_detail)).setText(recipe.getDescription());
        ((TextView) mView.findViewById(R.id.recipe_description)).setText(recipe.getDescription());
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
