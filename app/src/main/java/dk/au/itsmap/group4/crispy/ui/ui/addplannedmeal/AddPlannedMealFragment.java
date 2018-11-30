package dk.au.itsmap.group4.crispy.ui.ui.addplannedmeal;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dk.au.itsmap.group4.crispy.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AddPlannedMealFragment extends Fragment {

    private AddPlannedMealViewModel mViewModel;

    public static AddPlannedMealFragment newInstance() {
        return new AddPlannedMealFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_planned_meal_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddPlannedMealViewModel.class);
        // TODO: Use the ViewModel
    }

}
