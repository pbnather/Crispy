package dk.au.itsmap.group4.crispy.ui.mealsPlan.addPlannedMeal;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import com.google.firebase.Timestamp;

import java.util.Calendar;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.ui.mealsPlan.MealsPlanViewModel;

//from: https://developer.android.com/guide/topics/ui/controls/pickers
public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private MealsPlanViewModel mModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mModel = ViewModelProviders.of(getActivity()).get(MealsPlanViewModel.class);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = mModel.getSelectedMeal().getValue().getCalendarInstance();

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Calendar c = mModel.getSelectedMeal().getValue().getCalendarInstance();

        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);

        IMeal meal = mModel.getSelectedMeal().getValue();
        meal.setDate(new Timestamp(c.getTime()));

        mModel.getSelectedMeal().setValue(meal);
    }

}
