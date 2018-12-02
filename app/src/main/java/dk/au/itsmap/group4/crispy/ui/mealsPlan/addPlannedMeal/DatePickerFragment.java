package dk.au.itsmap.group4.crispy.ui.mealsPlan.addPlannedMeal;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import dk.au.itsmap.group4.crispy.ui.mealsPlan.MealsPlanViewModel;

//from: https://developer.android.com/guide/topics/ui/controls/pickers
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private MealsPlanViewModel mModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = ViewModelProviders.of(getActivity()).get(MealsPlanViewModel.class);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = mModel.getSelectedDate();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        mModel.getSelectedDate().set(Calendar.YEAR, year);
        mModel.getSelectedDate().set(Calendar.MONTH, month);
        mModel.getSelectedDate().set(Calendar.DAY_OF_MONTH, day);
    }
}
