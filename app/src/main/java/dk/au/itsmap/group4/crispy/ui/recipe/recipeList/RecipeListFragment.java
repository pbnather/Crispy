package dk.au.itsmap.group4.crispy.ui.recipe.recipeList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.ui.MainNavigationActivity;
import dk.au.itsmap.group4.crispy.ui.recipe.RecipeViewModel;
import dk.au.itsmap.group4.crispy.ui.recipe.recipeDetail.RecipeDetailFragment;
import dk.au.itsmap.group4.crispy.ui.recipe.recipeDetail.RecipeEditFragment;

public class RecipeListFragment extends Fragment implements RecipesRecyclerViewAdapter.OnRecipeClickListener {

    private View mView;
    private RecipeViewModel mModel;
    private RecipesRecyclerViewAdapter mAdapter;
    private MainNavigationActivity mActivity;
    private Guideline mGuideLine;
    private FloatingActionButton addRecipeButton;

    private RecipeDetailFragment mRecipeDetailFragment;
    private RecipeEditFragment mRecipeEditFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainNavigationActivity) this.getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.recipe_list_fragment, container, false);

        mModel = ViewModelProviders.of(mActivity).get(RecipeViewModel.class);

        // right column is visible only on some resolutions
        mModel.setIsSinglePage(mView.findViewById(R.id.right_column) != null);
        mModel.setMode(RecipeViewModel.Mode.LIST);

        // change right column based on ViewModel modes
        if(mModel.isSinglePage()) {
            mGuideLine = mView.findViewById(R.id.recipe_list_separator);
            setGuidelinePosition(1f);
            if(mRecipeEditFragment == null) {
                mRecipeEditFragment = new RecipeEditFragment();
            }
            if(mRecipeDetailFragment == null) {
                mRecipeDetailFragment = new RecipeDetailFragment();
            }
            addFragment(mRecipeDetailFragment);
            addFragment(mRecipeEditFragment);
            hideFragment(mRecipeDetailFragment);
            hideFragment(mRecipeEditFragment);
            mModel.getMode().observe(this, mode -> {
                switch (mode) {
                    case VIEW:
                        hideFragment(mRecipeEditFragment);
                        showFragment(mRecipeDetailFragment);
                        break;
                    case EDIT:
                    case ADD:
                        hideFragment(mRecipeDetailFragment);
                        showFragment(mRecipeEditFragment);
                        break;
                    case LIST:
                        hideFragment(mRecipeDetailFragment);
                        hideFragment(mRecipeEditFragment);
                        // show list in full page width
                        setGuidelinePosition(1f);
                }
            });
        }

        setupFloatingButton();
        setupRecyclerView();
        mActivity.setMainToolbarWithNavigation(getText(R.string.recipies).toString());

        return mView;

    }

    private void setupFloatingButton() {
        addRecipeButton = mView.findViewById(R.id.addRecipeButton);
        addRecipeButton.setOnClickListener(view -> {
            mModel.selectRecipe(null);
            if(mModel.isSinglePage()) {
                mModel.setMode(RecipeViewModel.Mode.ADD);
            } else {
                Navigation.findNavController(mView).navigate(R.id.recipeEditFragment);
            }
        });
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = mView.findViewById(R.id.recipe_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mAdapter = new RecipesRecyclerViewAdapter(mActivity, this);

        // observe model for changes
        mModel.getAllRecipes().observe(this, recipes -> mAdapter.setData(recipes));

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onRecipeClicked(IRecipe recipe) {
        if(mModel.isSinglePage()) {
            mModel.setMode(RecipeViewModel.Mode.VIEW);
            mModel.selectRecipe(recipe);
        } else {
            mModel.selectRecipe(recipe);
            Navigation.findNavController(mView).navigate(R.id.recipeDetailFragment);
        }
    }


    private void addFragment(Fragment fragment) {
        if(mView.findViewById(R.id.right_column) != null) {
            setGuidelinePosition(0.45f);
            mActivity.getSupportFragmentManager().beginTransaction()
                    .add(R.id.right_column, fragment)
                    .commit();
        }
    }
    private void hideFragment(Fragment fragment) {
        if(mView.findViewById(R.id.right_column) != null) {
            setGuidelinePosition(0.45f);
            mActivity.getSupportFragmentManager().beginTransaction()
                    .hide(fragment)
                    .commit();
        }
    }

    private void showFragment(Fragment fragment) {
        if(mView.findViewById(R.id.right_column) != null) {
            setGuidelinePosition(0.45f);
            mActivity.getSupportFragmentManager().beginTransaction()
                    .show(fragment)
                    .commit();
        }
    }

    private void setGuidelinePosition(float percentage) {
        if(mGuideLine == null || percentage < 0 || percentage > 1) {
            return;
        }
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) mGuideLine.getLayoutParams();
        params.guidePercent = percentage; // 45% // range: 0 <-> 1
        mGuideLine.setLayoutParams(params);
    }

}
