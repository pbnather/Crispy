package dk.au.itsmap.group4.crispy.ui.recipe.recipeList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.database.entity.Recipe;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.ui.MainNavigationActivity;
import dk.au.itsmap.group4.crispy.ui.recipe.RecipeViewModel;

public class RecipeListFragment extends Fragment implements RecipesRecyclerViewAdapter.OnRecipeClickListener {

    private MainNavigationActivity mActivity;
    private View mView;

    private RecipeViewModel mModel;

    private RecipesRecyclerViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = ViewModelProviders.of(getActivity()).get(RecipeViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mView = inflater.inflate(R.layout.recipe_list_fragment, container, false);
        mActivity = (MainNavigationActivity) this.getActivity();

        mActivity.setMainToolbarWithNavigation("Recipies");

        FloatingActionButton addRecipeButton = (FloatingActionButton) mView.findViewById(R.id.addRecipeButton);
        addRecipeButton.setOnClickListener(view -> {
            Recipe newRecipe = new Recipe();
            mModel.selectRecipe(newRecipe.getId());
            Navigation.findNavController(mView).navigate(R.id.recipeEditFragment);
        });

        // setup list adapters
        setupRecyclerView();

        // observe for model changes
        mModel.getAllRecipes().observe(this, (recipeList) -> {
            if(mAdapter != null) {
                mAdapter.setData(recipeList);
            }
        });

        return mView;

    }

    private void setupRecyclerView() {
        mRecyclerView = mView.findViewById(R.id.recipe_list);
        assert mRecyclerView != null;

        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new RecipesRecyclerViewAdapter(getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);
    }


    @Override
    public void onRecipeClicked(IRecipe recipe) {

        mModel.selectRecipe(recipe.getId());

        Navigation.findNavController(mView).navigate(R.id.recipeDetailFragment);
    }

}
