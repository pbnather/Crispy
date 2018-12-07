package dk.au.itsmap.group4.crispy.ui.mealsPlan.addPlannedMeal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.model.IUserGroup;
import dk.au.itsmap.group4.crispy.service.GlideApp;
import dk.au.itsmap.group4.crispy.ui.IAccountManager;
import dk.au.itsmap.group4.crispy.ui.MainNavigationActivity;
import dk.au.itsmap.group4.crispy.ui.mealsPlan.MealsPlanViewModel;
import dk.au.itsmap.group4.crispy.utils.Helpers;

public class AddPlannedMealFragment extends Fragment {

    private IAccountManager mAccount;
    private MealsPlanViewModel mModel;
    private boolean mIsEditMode = false;
    private IMeal mMeal;

    private MainNavigationActivity mActivity;
    private Button btnSave, btnDate, btnTime, btnDelete;
    private TextView mealDate, mealTime;
    private Spinner whoCooksSpinner;
    private View mView;

    private AutoCompleteRecipeAdapter mRecipesAutoCompleteAdapter;
    private UserAdapter mUsersSpinnerAdapter;
    private AutoCompleteTextView recipeName;

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
        mActivity = (MainNavigationActivity) this.getActivity();
        mAccount = mActivity;
        mView = inflater.inflate(R.layout.add_planned_meal_fragment, container, false);

        btnSave = mView.findViewById(R.id.btnSave);
        btnDate = mView.findViewById(R.id.btnDate);
        btnTime = mView.findViewById(R.id.btnTime);
        btnDelete = mView.findViewById(R.id.btnDelete);


        recipeName = mView.findViewById(R.id.recipeName);

        mealDate = mView.findViewById(R.id.mealDate);
        mealTime = mView.findViewById(R.id.mealTime);


        String toolbar_name;
        if (mIsEditMode)
            toolbar_name= getText(R.string.edit_meal).toString();
        else
            toolbar_name = getText(R.string.add_meal).toString();

        mActivity.setMainToolbarWithNavigation(toolbar_name);


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
        recipeName.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                IRecipe selectedRecipe = (IRecipe) parent.getItemAtPosition(position);
                mMeal.setTitle(selectedRecipe.getTitle());
                mMeal.setImage_url(selectedRecipe.getImage_url());
                mModel.getSelectedMeal().setValue(mMeal);
            }
        });

        // assign to user
        mUsersSpinnerAdapter = new UserAdapter(mActivity, R.layout.group_member_item, R.id.userName);
        mAccount.getUserGroup().observe(mActivity, group -> mUsersSpinnerAdapter.setData(group.getAllUsers()));
        whoCooksSpinner = mView.findViewById(R.id.whoPrepares);
        whoCooksSpinner.setAdapter(mUsersSpinnerAdapter);
        whoCooksSpinner.setSelection(mUsersSpinnerAdapter.getPosition(mModel.getSelectedMeal().getValue().getCookName()));
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
        // save meal changes before showing the dialog
        mMeal.setTitle(recipeName.getText().toString());
        mModel.getSelectedMeal().setValue(mMeal);

        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(),"timePicker");
    }

    private void showDatePickerDialog(View v) {
        // save meal changes before showing the dialog
        mMeal.setTitle(recipeName.getText().toString());
        mModel.getSelectedMeal().setValue(mMeal);
        
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private void updateView(IMeal meal) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d. MMM");

        mealDate.setText(sdf.format(meal.getDate()));

        mealTime.setText(DateUtils.formatDateTime(mActivity, meal.getDate().getTime(), DateUtils.FORMAT_SHOW_TIME));


        recipeName.setText(meal.getTitle());

    }

    /* Adapted from http://android-er.blogspot.com/2010/12/custom-arrayadapter-for-spinner-with.html */
    public class UserAdapter extends ArrayAdapter<String> {
        private List<Map<String, String>> mUsers;
        private List<String> mUsernames;

        public UserAdapter(@NonNull Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        public void setData(List<Map<String, String>> users) {
            mUsers = users;
            mUsernames = new ArrayList<>();
            for(Map<String, String> user : mUsers) {
                mUsernames.add(user.get("name").split(" ")[0]);
            }
            clear();
            addAll(mUsernames);
            notifyDataSetChanged();
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.group_member_item, parent, false);

            TextView userName = row.findViewById(R.id.userName);
            ImageView userPhoto = row.findViewById(R.id.userPhoto);

            String photoUrl = mUsers.get(position).get("photo_url");
            userName.setText(mUsers.get(position).get("name").split(" ")[0]);

            GlideApp.with(row)
                    .load(photoUrl)
                    .placeholder(R.drawable.crispy_icon)
                    .into(userPhoto);

            return row;
        }
    }

}



