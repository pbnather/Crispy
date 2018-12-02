package dk.au.itsmap.group4.crispy.ui.mealsPlan.addPlannedMeal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.ui.mealsPlan.MealsPlanViewModel;
import dk.au.itsmap.group4.crispy.utils.Helpers;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class AddPlannedMealFragment extends Fragment {

    private MealsPlanViewModel mModel;

    private Activity mActivity;
    private Button btnSave, btnDate, btnTime;
    private TextView mealDate, mealTime;
    private Spinner whoCooksSpinner;
    private View mView;

    private AutoCompleteAdapter mRecipesAutoCompleteAdapter;
    private ArrayAdapter<String> mUsersSpinnerAdapter;
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

        btnSave = mView.findViewById(R.id.btnSave);
        btnDate = mView.findViewById(R.id.btnDate);
        btnTime = mView.findViewById(R.id.btnTime);

        recipeName = mView.findViewById(R.id.recipeName);

        mealDate = mView.findViewById(R.id.mealDate);
        mealTime = mView.findViewById(R.id.mealTime);

        mModel.getSelectedMeal().observe(this, meal -> {

            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d. MMM");

            mealDate.setText(sdf.format(meal.getDate()));

            mealTime.setText(String.format(DateFormat.getTimeInstance(DateFormat.SHORT).format(meal.getDate())));
        });

        // select recipe
        mRecipesAutoCompleteAdapter = new AutoCompleteAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line);
        recipeName.setAdapter(mRecipesAutoCompleteAdapter);
        mModel.getAllRecipes().observe(this,
                recipes -> mRecipesAutoCompleteAdapter.setData(recipes)
        );
        // has selected recipe from autocomplete
        recipeName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                IMeal meal = mModel.getSelectedMeal().getValue();
                if(meal != null) {
                    meal.setTitle(parent.getItemAtPosition(position).toString());
                    mModel.getSelectedMeal().setValue(meal);
                }
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        // has selected by clicking outside
        recipeName.setOnFocusChangeListener((View v, boolean b) -> {
            if(!b) {
                // put selected recipe to VM
                IMeal meal = mModel.getSelectedMeal().getValue();
                if(meal != null) {
                    meal.setTitle(((TextView) v).getText().toString());
                    mModel.getSelectedMeal().setValue(meal);
                }
            }
        });

        // assign to user
        mUsersSpinnerAdapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_spinner_item, mModel.getPossibleCooks());
        whoCooksSpinner = mView.findViewById(R.id.whoPrepares);
        whoCooksSpinner.setAdapter(mUsersSpinnerAdapter);
        whoCooksSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                IMeal meal = mModel.getSelectedMeal().getValue();
                if(meal != null) {
                    meal.setCookName(parent.getItemAtPosition(position).toString());
                    mModel.getSelectedMeal().setValue(meal);
                }
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        // click handlers
        btnDate.setOnClickListener(v -> showDatePickerDialog(v));
        btnTime.setOnClickListener(v -> showTimePickerDialog(v));

        btnSave.setOnClickListener(v -> {
            savingHandler();
        });

        return mView;
    }

    private void savingHandler() {
        // close the keyboard
        Helpers.hideKeyboard(mActivity);

        // save the meal
        IMeal meal = mModel.getSelectedMeal().getValue();
        if(meal != null) {
            meal.setTitle((recipeName).getText().toString());
            mModel.getSelectedMeal().setValue(meal);

            if (meal.getDate().compareTo(new Date()) < 0) {
                Toast.makeText(getActivity(), "Focus more on future, not on the past!\nChange the time of meal.", Toast.LENGTH_LONG).show();
            } else {
                mModel.createMeal();
                Toast.makeText(getActivity(), "Meal was saved!", Toast.LENGTH_LONG).show();
                Navigation.findNavController(mView).navigateUp();
            }
        }
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



