package dk.au.itsmap.group4.crispy.ui.mealsPlan.addPlannedMeal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.ui.mealsPlan.MealsPlanViewModel;

public class AddPlannedMealFragment extends Fragment {

    private MealsPlanViewModel mModel;

    private Activity mActivity;
    private Button btnSave, btnCancel, btnDate, btnTime;
    private TextView mealDate, mealTime;
    private Spinner whoCooksSpinner;
    private View mView;

    private AutoCompleteAdapter mRecipesAutoCompleteAdapter;
    private AutoCompleteTextView recipeName;

    public static AddPlannedMealFragment newInstance() {
        return new AddPlannedMealFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = ViewModelProviders.of(getActivity()).get(MealsPlanViewModel.class);
    }

    @SuppressLint("DefaultLocale")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mActivity = this.getActivity();
        mView = inflater.inflate(R.layout.add_planned_meal_fragment, container, false);

        btnCancel = mView.findViewById(R.id.btnCancel);
        btnSave = mView.findViewById(R.id.btnSave);
        btnDate = mView.findViewById(R.id.btnDate);
        btnTime = mView.findViewById(R.id.btnTime);

        recipeName = mView.findViewById(R.id.recipeName);

        mealDate = mView.findViewById(R.id.mealDate);
        mealTime = mView.findViewById(R.id.mealTime);

        mModel.getSelectedDate().observe(this, calendar -> {
            mealDate.setText(calendar.getTime().toString());

            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d. MMM");

            mealDate.setText(sdf.format(calendar.getTime()));

            mealTime.setText(String.format(DateFormat.getTimeInstance(DateFormat.SHORT).format(calendar.getTime())));
        });

        // select recipe
        mModel.getAllRecipes().observe(this, recipes -> mRecipesAutoCompleteAdapter.setData(recipes));

        mRecipesAutoCompleteAdapter = new AutoCompleteAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line);
        recipeName.setAdapter(mRecipesAutoCompleteAdapter);

        whoCooksSpinner = mView.findViewById(R.id.whoPrepares);
        whoCooksSpinner.setAdapter(mRecipesAutoCompleteAdapter);

        recipeName.setOnFocusChangeListener((View v, boolean b) -> {
            if(!b) {
                // put selected recipe to VM
                mModel.setSelectedRecipe(((TextView)v).getText().toString());
            }
        });

        btnDate.setOnClickListener(v -> showDatePickerDialog(v));
        btnTime.setOnClickListener(v -> showTimePickerDialog(v));

        btnSave.setOnClickListener(v -> {
            savingHandler();

            Navigation.findNavController(mView).navigateUp();

        });

        btnCancel.setOnClickListener(v -> Navigation.findNavController(mView).navigateUp());

        return mView;
    }

    private void savingHandler() {
        mModel.createMeal();
        Toast.makeText(getActivity(), "Meal was saved!", Toast.LENGTH_LONG).show();
    }


    private void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(),"timePicker");
    }

    private void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

}



