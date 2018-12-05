package dk.au.itsmap.group4.crispy.ui.recipe.recipeDetail;

import android.app.Activity;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IIngredient;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.ui.MainNavigationActivity;
import dk.au.itsmap.group4.crispy.ui.recipe.RecipeViewModel;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class RecipeDetailFragment extends Fragment {

    private RecipeViewModel mModel;
    private TableLayout ingredientsTable;
    private MainNavigationActivity mActivity;
    private View mView;
    private FloatingActionButton btnEditRecipe;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.recipe_detail_fragment, container, false);
        mActivity = (MainNavigationActivity) this.getActivity();

        //close main toolbar
//        Toolbar toolbar = (Toolbar) mActivity.findViewById(R.id.mainToolbar);
//        toolbar.setVisibility(View.GONE);

         Toolbar superToolbar = mView.findViewById(R.id.detail_toolbar);
//        superToolbar.setTitle(R.string.crispy_planner);


//        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//
//        //actionBar.setTitle(R.string.recipe_detail_title);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);

        mActivity.setToolbar(superToolbar);

        // inflate inner layout to scroll view
        inflater.inflate(R.layout.recipe_detail_inner_detail, mView.findViewById(R.id.recipe_detail_container));

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
            CollapsingToolbarLayout appBarLayout = mActivity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(recipe.getTitle());
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
