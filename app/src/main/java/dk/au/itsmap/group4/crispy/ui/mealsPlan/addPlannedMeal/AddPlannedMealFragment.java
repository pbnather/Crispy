package dk.au.itsmap.group4.crispy.ui.mealsPlan.addPlannedMeal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.ui.mealsPlan.MealsPlanViewModel;
import dk.au.itsmap.group4.crispy.utils.Helpers;

public class AddPlannedMealFragment extends Fragment {

    private MealsPlanViewModel mModel;
    private boolean mIsEditMode = false;
    private IMeal mMeal;

    private Activity mActivity;
    private Button btnSave, btnDate, btnTime, btnDelete;
    private TextView mealDate, mealTime;
    private Spinner whoCooksSpinner;
    private View mView;

    private AutoCompleteRecipeAdapter mRecipesAutoCompleteAdapter;
    private ArrayAdapter<String> mUsersSpinnerAdapter;
    private AutoCompleteTextView recipeName;

    public static AddPlannedMealFragment newInstance() {
        return new AddPlannedMealFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = ViewModelProviders.of(getActivity()).get(MealsPlanViewModel.class);

        mIsEditMode = mModel.getMode() == MealsPlanViewModel.Mode.EDIT;

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
        btnDelete = mView.findViewById(R.id.btnDelete);


        recipeName = mView.findViewById(R.id.recipeName);

        mealDate = mView.findViewById(R.id.mealDate);
        mealTime = mView.findViewById(R.id.mealTime);

        Toolbar toolbar = (Toolbar) mActivity.findViewById(R.id.mainToolbar);
        toolbar.setTitle(R.string.add_meal);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        mModel.getSelectedMeal().observe(this, meal -> {
            mMeal = meal;
            updateView(meal);
        });

        // select recipe
        mRecipesAutoCompleteAdapter = new AutoCompleteRecipeAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line);
        recipeName.setAdapter(mRecipesAutoCompleteAdapter);
        mModel.getAllRecipes().observe(this,
                recipes -> mRecipesAutoCompleteAdapter.setData(recipes)
        );
        // has selected recipe from autocomplete
        recipeName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                mMeal.setTitle(parent.getItemAtPosition(position).toString());
                mModel.getSelectedMeal().setValue(mMeal);
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });
        // has selected by clicking outside
        recipeName.setOnFocusChangeListener((View v, boolean b) -> {
            if(!b) {
                mMeal.setTitle(((TextView) v).getText().toString());
                mModel.getSelectedMeal().setValue(mMeal);
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
                mMeal.setCookName(parent.getItemAtPosition(position).toString());
                mModel.getSelectedMeal().setValue(mMeal);
            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });

        // click handlers
        if(mIsEditMode) {
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(v -> deletingHandler());
        }

        btnDate.setOnClickListener(v -> showDatePickerDialog(v));
        btnTime.setOnClickListener(v -> showTimePickerDialog(v));

        btnSave.setOnClickListener(v -> {
            savingHandler();
        });

        return mView;
    }

    private void deletingHandler() {
        // close the keyboard
        Helpers.hideKeyboard(mActivity);

        mModel.deleteSelectedMeal();
        Toast.makeText(getActivity(), R.string.delete_meal_message, Toast.LENGTH_LONG).show();
        Navigation.findNavController(mView).navigateUp();
    }

    private void savingHandler() {
        // close the keyboard
        Helpers.hideKeyboard(mActivity);

        // save the meal
        mMeal.setTitle((recipeName).getText().toString());
        mModel.getSelectedMeal().setValue(mMeal);

        if (mMeal.getDate().compareTo(new Date()) < 0) {
            Toast.makeText(getActivity(), R.string.add_meal_is_in_past_error, Toast.LENGTH_LONG).show();
        } else {
            mModel.createMeal();
            Toast.makeText(getActivity(), mIsEditMode ? R.string.add_meal_update_success : R.string.add_meal_save_success, Toast.LENGTH_LONG).show();
            Navigation.findNavController(mView).navigateUp();
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

    private void updateView(IMeal meal) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d. MMM");

        mealDate.setText(sdf.format(meal.getDate()));

        mealTime.setText(String.format(DateFormat.getTimeInstance(DateFormat.SHORT).format(meal.getDate())));

        recipeName.setText(meal.getTitle());
    }

}



