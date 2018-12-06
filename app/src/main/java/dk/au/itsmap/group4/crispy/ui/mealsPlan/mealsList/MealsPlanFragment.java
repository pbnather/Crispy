package dk.au.itsmap.group4.crispy.ui.mealsPlan.mealsList;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.ui.MainNavigationActivity;
import dk.au.itsmap.group4.crispy.ui.mealsPlan.MealsPlanViewModel;


public class MealsPlanFragment extends Fragment implements MealsPlanRecyclerViewAdapter.OnRecyclerViewItemClickListener {

    private MealsPlanRecyclerViewAdapter mAdapter;
    private MainNavigationActivity mActivity;
    private Button btnRecipies;
    private FloatingActionButton btnAddMeal;
    private View mView;

    private MealsPlanViewModel mModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = (MainNavigationActivity) this.getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.meals_plan_fragment, container, false);
        mModel = ViewModelProviders.of(mActivity).get(MealsPlanViewModel.class);

        setupRecyclerView();

        btnRecipies = mView.findViewById(R.id.btnAllRecipies);
        btnAddMeal = mView.findViewById(R.id.btnAddMeal);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(R.string.crispy_planner);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);

        // all recipes button
        btnRecipies.setOnClickListener(v -> {
            Navigation.findNavController(mView).navigate(R.id.recipeListFragment);
        });

        // add meal to plan button
        btnAddMeal.setOnClickListener(v -> {
            mModel.switchToAddMode();
            Navigation.findNavController(mView).navigate(R.id.addPlannedMealFragment);
        });

        return mView;
    }


    private void setupRecyclerView() {
        RecyclerView mRecyclerView = mView.findViewById(R.id.daysList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity);
        mAdapter = new MealsPlanRecyclerViewAdapter(this);

        // observe for model changes
        mModel.getAllMeals().observe(mActivity, (meal) -> mAdapter.setData(meal));

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClicked(IMeal meal) {
        mModel.switchToEditMode(meal);
        Navigation.findNavController(mView).navigate(R.id.addPlannedMealFragment);
    }

}