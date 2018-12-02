package dk.au.itsmap.group4.crispy.ui.ui.mealsplan;

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
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.ui.AddPlannedMeal;
import dk.au.itsmap.group4.crispy.ui.ui.recipe.RecipeListActivity;


public class MealsPlanFragment extends Fragment {

    private MealsPlanViewModel mViewModel;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Activity mActivity;
    private Button btnRecipies;
    private FloatingActionButton btnAddMeal;
    private View mView;


    public static MealsPlanFragment newInstance() {
        return new MealsPlanFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.meals_plan_fragment, container, false);
        mActivity = this.getActivity();


        btnRecipies = mView.findViewById(R.id.btnAllRecipies);
        btnAddMeal = mView.findViewById(R.id.btnAddMeal);
        btnRecipies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, RecipeListActivity.class);
                mActivity.startActivity(intent);
            }
        });
        btnAddMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, AddPlannedMeal.class);
                mActivity.startActivity(intent);
            }
        });
        mRecyclerView = mView.findViewById(R.id.daysList);
        assert mRecyclerView != null;
        setupRecyclerView();
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MealsPlanViewModel.class);
        // TODO: Use the ViewModel
    }

    private void setupRecyclerView() {

        mLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(mLayoutManager);

        String[] data = {"Date1", "Date2","Date3","Date4","Date5"};
        mAdapter = new MyAdapter(data);
        mRecyclerView.setAdapter(mAdapter);
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private String[] mDataset;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: send meal id with the intent to be able to fill the form
                Intent intent = new Intent(mActivity, AddPlannedMeal.class);
                mActivity.startActivity(intent);
            }
        };

        public class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public TextView mDay;

            public MyViewHolder(View v) {
                super(v);
                mDay = v.findViewById(R.id.day_date);
            }
        }

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(String[] myDataset) {
            mDataset = myDataset;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            // create a new view
            View v = (View) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.meals_plan_meal_content, parent, false);
            MyViewHolder vh = new MyViewHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            // - get element from your dataset at this position
            // - replace the contents of the view with that element
            holder.mDay.setText(mDataset[position]);
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mDataset.length;
        }
    }
}
