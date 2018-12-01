package dk.au.itsmap.group4.crispy.ui.ui.grocerylist;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import dk.au.itsmap.group4.crispy.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GroceryListFragment extends Fragment {

    private GroceryListViewModel mViewModel;

    public static GroceryListFragment newInstance() {
        return new GroceryListFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.grocery_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(GroceryListViewModel.class);
        // TODO: Use the ViewModel
    }

}
