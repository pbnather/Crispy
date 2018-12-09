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
        mActivity.setMainToolbarWithNavigation(getText(R.string.recipies).toString());

        // right column is visible only on some resolutions
        mModel = ViewModelProviders.of(mActivity).get(RecipeViewModel.class);
        mModel.setIsSinglePage(mView.findViewById(R.id.right_column) != null);
        mModel.setMode(RecipeViewModel.Mode.LIST);

        // change right column based on ViewModel modes
        if(mModel.isSinglePage()) {
            mGuideLine = mView.findViewById(R.id.recipe_list_separator);
            setGuidelinePosition(1f);

            mModel.getMode().observe(this, mode -> {
                switch (mode) {
                    case VIEW:
                        showFragment(new RecipeDetailFragment());
                        break;
                    case EDIT:
                    case ADD:
                        showFragment(new RecipeEditFragment());
                        break;
                    case LIST:
                        // show list in full page width
                        setGuidelinePosition(1f);
                        break;
                }
            });
        }

        setupFloatingButton();
        setupRecyclerView();

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
        mModel.getAllRecipes().observe(mActivity, recipes -> mAdapter.setData(recipes));

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


    private void showFragment(Fragment fragment) {
        if(mView.findViewById(R.id.right_column) != null) {
            setGuidelinePosition(0.5f);
            mActivity.getSupportFragmentManager().beginTransaction()
                    .replace(R.id.right_column, fragment)
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
