package dk.au.itsmap.group4.crispy.ui.mealsPlan.addPlannedMeal;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.database.entity.Meal;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.ui.mealsPlan.MealsPlanViewModel;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class AddPlannedMealFragment extends Fragment {

    private MealsPlanViewModel mModel;

    private Activity mActivity;
    private Button btnSave, btnCancel, btnDate, btnTime;
    private View mView;


    public static AddPlannedMealFragment newInstance() {
        return new AddPlannedMealFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = ViewModelProviders.of(this).get(MealsPlanViewModel.class);
    }

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

        btnDate.setOnClickListener(v -> showDatePickerDialog(v));
        btnTime.setOnClickListener(v -> showTimePickerDialog(v));

        btnSave.setOnClickListener(v -> {
            savingHandler();
            mActivity.finish();
        });

        btnCancel.setOnClickListener(v -> mActivity.finish());

        return mView;
    }

    private void savingHandler() {
        IMeal meal = new Meal();

        // TODO: put values from the form to IMeal object

        mModel.saveMeal(meal);
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



