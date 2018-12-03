package dk.au.itsmap.group4.crispy.ui.mealsPlan.mealsList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.ui.mealsPlan.MealsPlanViewModel;
import dk.au.itsmap.group4.crispy.ui.recipe.recipeList.RecipeListActivity;
import dk.au.itsmap.group4.crispy.ui.GenericRecyclerViewAdapter;


public class MealsPlanFragment extends Fragment implements GenericRecyclerViewAdapter.OnRecyclerViewItemClickListener<IMeal> {

    private RecyclerView mRecyclerView;
    private GenericRecyclerViewAdapter<IMeal> mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Activity mActivity;
    private Button btnRecipies;
    private FloatingActionButton btnAddMeal;
    private View mView;

    private MealsPlanViewModel mModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = ViewModelProviders.of(getActivity()).get(MealsPlanViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.meals_plan_fragment, container, false);
        mActivity = this.getActivity();
        btnRecipies = mView.findViewById(R.id.btnAllRecipies);
        btnAddMeal = mView.findViewById(R.id.btnAddMeal);
        Toolbar toolbar = (Toolbar) mActivity.findViewById(R.id.mainToolbar);
        toolbar.setTitle(R.string.crispy_planner);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(false);

        // all recipes button
        btnRecipies.setOnClickListener(v -> {
            Intent intent = new Intent(mActivity, RecipeListActivity.class);
            mActivity.startActivity(intent);
        });

        // add meal to plan button
        btnAddMeal.setOnClickListener(v -> {
            Navigation.findNavController(mView).navigate(R.id.addPlannedMealFragment);
        });

        // setup list view
        mRecyclerView = mView.findViewById(R.id.daysList);
        assert mRecyclerView != null;
        setupRecyclerView();

        // observe for model changes
        mModel.getAllMeals().observe(this, (meal) -> {
            if(mAdapter != null) {
                mAdapter.setData(meal);
            }
        });

        return mView;
    }


    private void setupRecyclerView() {

        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MealsPlanRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClicked(IMeal meal) {
        mModel.getSelectedMeal().setValue(meal);
        Navigation.findNavController(mView).navigate(R.id.addPlannedMealFragment);
    }

}
