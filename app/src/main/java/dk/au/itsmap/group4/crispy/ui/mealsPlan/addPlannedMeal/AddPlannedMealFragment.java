package dk.au.itsmap.group4.crispy.ui.mealsPlan.addPlannedMeal;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
    private TextView recipeName;
    private Spinner recipeSpinner;
    private View mView;


    public static AddPlannedMealFragment newInstance() {
        return new AddPlannedMealFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = ViewModelProviders.of(getActivity()).get(MealsPlanViewModel.class);
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

        recipeName = mView.findViewById(R.id.recipeName);

        // Todo: missing recipe spinner in view
        // recipeSpinner = (Spinner) mView.findViewById(R.id.);
        mModel.getRecipeById("4X5J3QNDEldBH8hGKaIs").observe(this, recipe -> mModel.setSelectedRecipe(recipe));


        btnDate.setOnClickListener(v -> showDatePickerDialog(v));
        btnTime.setOnClickListener(v -> showTimePickerDialog(v));

        btnSave.setOnClickListener(v -> {
            savingHandler();

            Navigation.findNavController(mView).navigateUp();

            // ((MealsPlanActivity)mActivity).goBackToList();
            // mActivity.finish();
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



