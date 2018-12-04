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
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.ui.mealsPlan.MealsPlanViewModel;


public class MealsPlanFragment extends Fragment implements MealsPlanRecyclerViewAdapter.OnRecyclerViewItemClickListener {

    private RecyclerView mRecyclerView;
    private MealsPlanRecyclerViewAdapter mAdapter;
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

        // setup list view
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
        mRecyclerView = mView.findViewById(R.id.daysList);
        assert mRecyclerView != null;

        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MealsPlanRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClicked(IMeal meal) {
        mModel.switchToEditMode(meal);
        Navigation.findNavController(mView).navigate(R.id.addPlannedMealFragment);
    }

}