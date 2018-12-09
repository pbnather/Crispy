package dk.au.itsmap.group4.crispy.ui.recipe.recipeDetail;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.os.Bundle;

import androidx.annotation.NonNull;
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

import java.util.List;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IIngredient;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.utils.GlideApp;
import dk.au.itsmap.group4.crispy.ui.MainNavigationActivity;
import dk.au.itsmap.group4.crispy.ui.recipe.RecipeViewModel;

public class RecipeDetailFragment extends Fragment {

    private RecipeViewModel mModel;
    private TableLayout ingredientsTable;
    private MainNavigationActivity mActivity;
    private View mView;
    private FloatingActionButton btnEditRecipe;
    private ImageView mRecipeToolbarImage;

    private IRecipe mRecipe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainNavigationActivity) this.getActivity();
        mModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);
        if (!mModel.isSinglePage())
            setHasOptionsMenu(true);
        mActivity = (MainNavigationActivity) this.getActivity();
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

        mView = inflater.inflate(R.layout.recipe_detail_fragment, container, false);

        Toolbar superToolbar = mView.findViewById(R.id.detail_toolbar);
        mRecipeToolbarImage = mView.findViewById(R.id.recipeImageToolbar);

        if(!mActivity.isOrientationLandscape()) {
            mActivity.setToolbar(superToolbar); // TODO: fix toolbar in landscape
        }

        // inflate inner layout to scroll view
        inflater.inflate(R.layout.recipe_detail_inner_detail, mView.findViewById(R.id.recipe_detail_container));

        btnEditRecipe = mView.findViewById(R.id.btnEditRecipe);
        btnEditRecipe.setOnClickListener(v -> {
            mModel.setMode(RecipeViewModel.Mode.EDIT);
//            mModel.selectRecipe(mRecipe);
            if(mModel.isSinglePage()) {
//                Navigation.findNavController(mView).popBackStack();
//                mActivity.getNavController().navigate(R.id.recipeListFragment);
            } else {
                Navigation.findNavController(mView).navigate(R.id.recipeEditFragment);
            }
        });

        ingredientsTable = mView.findViewById(R.id.ingredientsTable);

        mModel.getSelectedRecipe().observe(mActivity, (recipe) -> {
            mRecipe = recipe;
            updateView(mRecipe);
        });

        mModel.getIngredientsForSelectedRecipe().observe(mActivity, ingredients -> {
            ingredientsTable.removeAllViews();
            if(ingredients == null) {
                return;
            }
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
        // set title to app bar or ot custom headline in landscape
        CollapsingToolbarLayout appBarLayout = mView.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(recipe.getTitle());
            GlideApp.with(mView)
                    .load(recipe.getImage_url())
                    .centerCrop()
                    .into(mRecipeToolbarImage);
        }
        TextView txtTitle = mView.findViewById(R.id.txtRecipeTitle);
        if(txtTitle != null) {
            txtTitle.setText(recipe.getTitle());
        }

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
