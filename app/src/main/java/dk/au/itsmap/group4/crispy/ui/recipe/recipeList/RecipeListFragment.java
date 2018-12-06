package dk.au.itsmap.group4.crispy.ui.recipe.recipeList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.ui.MainNavigationActivity;
import dk.au.itsmap.group4.crispy.ui.recipe.RecipeViewModel;

public class RecipeListFragment extends Fragment implements RecipesRecyclerViewAdapter.OnRecipeClickListener {

    private View mView;
    private RecipeViewModel mModel;
    private RecipesRecyclerViewAdapter mAdapter;
    private MainNavigationActivity mActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainNavigationActivity) this.getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mActivity = (MainNavigationActivity) this.getActivity();
        mActivity.setMainToolbarWithNavigation(getText(R.string.recipies).toString());
        mView = inflater.inflate(R.layout.recipe_list_fragment, container, false);

        mModel = ViewModelProviders.of(mActivity).get(RecipeViewModel.class);

        setupFloatingButton();
        setupRecyclerView();
        return mView;

    }

    private void setupFloatingButton() {
        FloatingActionButton addRecipeButton = mView.findViewById(R.id.addRecipeButton);
        addRecipeButton.setOnClickListener(view -> {

            mModel.selectRecipe(null);
            Navigation.findNavController(mView).navigate(R.id.recipeEditFragment);
        });
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = mView.findViewById(R.id.recipe_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mAdapter = new RecipesRecyclerViewAdapter(mActivity, this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);

        // observe model for changes
        mModel.getAllRecipes().observe(mActivity, recipes -> mAdapter.setData(recipes));
    }

    @Override
    public void onRecipeClicked(IRecipe recipe) {

        mModel.selectRecipe(recipe.getId());
        Navigation.findNavController(mView).navigate(R.id.recipeDetailFragment);
    }

}
