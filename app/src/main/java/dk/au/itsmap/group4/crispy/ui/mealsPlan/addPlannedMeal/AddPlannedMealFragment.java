package dk.au.itsmap.group4.crispy.ui.mealsPlan.addPlannedMeal;

import android.app.MediaRouteActionProvider;
import android.content.Context;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IMeal;
import dk.au.itsmap.group4.crispy.model.IRecipe;
import dk.au.itsmap.group4.crispy.model.IUserGroup;
import dk.au.itsmap.group4.crispy.ui.IAccountManager;
import dk.au.itsmap.group4.crispy.ui.INavigationController;
import dk.au.itsmap.group4.crispy.ui.mealsPlan.MealsPlanViewModel;
import dk.au.itsmap.group4.crispy.utils.GlideApp;

public class AddPlannedMealFragment extends Fragment {

    private static final String TIME_PICKER_TAG = "TimePicker";
    private static final String DATE_PICKER_TAG = "TimePicker";

    private FragmentManager mFragmentManager;
    private MealsPlanViewModel mModel;
    private IUserGroup mUserGroup;
    private IMeal mMeal;

    private IAccountManager mAccount;
    private INavigationController mNavigation;
    private AppCompatActivity mActivity;
    private TextView mMealDate, mMealTime;
    private Spinner mGroupMembers;
    private View mView;

    private AutoCompleteRecipeAdapter mRecipesAdapter;
    private AutoCompleteTextView mRecipeName;

    private boolean mIsEditMode = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (AppCompatActivity) this.getActivity();
        mAccount = (IAccountManager) mActivity;
        mNavigation = (INavigationController) mActivity;
        mFragmentManager = this.getFragmentManager();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.meals_plan_edit_fragment, container, false);
        mModel = ViewModelProviders.of(mActivity).get(MealsPlanViewModel.class);
        mIsEditMode = mModel.getMode() == MealsPlanViewModel.Mode.EDIT;

        mGroupMembers = mView.findViewById(R.id.whoPrepares);
        mRecipeName = mView.findViewById(R.id.recipeName);
        mMealDate = mView.findViewById(R.id.mealDate);
        mMealTime = mView.findViewById(R.id.mealTime);

        // observe selected meal for changes
        mModel.getSelectedMeal().observe(mActivity, meal -> {
            mMeal = meal;
            updateView();
        });

        setupToolbar();
        setupButtons();
        setupRecipePicker();
        setupGroupMemberPicker();

        return mView;
    }

    private void setupButtons() {
        // get buttons views
        Button btnSave = mView.findViewById(R.id.btnSave);
        Button btnDate = mView.findViewById(R.id.btnDate);
        Button btnTime = mView.findViewById(R.id.btnTime);
        Button btnDelete = mView.findViewById(R.id.btnDelete);

        // setup click handlers
        btnDate.setOnClickListener(v -> showDatePickerDialog());
        btnTime.setOnClickListener(v -> showTimePickerDialog());
        btnDelete.setOnClickListener(v -> deletingHandler());
        btnSave.setOnClickListener(v -> savingHandler());

        // hide delete button if not in edit mode
        if(mIsEditMode) btnDelete.setVisibility(View.VISIBLE);
    }

    private void setupToolbar() {
        String toolbarName = mIsEditMode ?
                getText(R.string.edit_meal).toString() :
                getText(R.string.add_meal).toString();
        mNavigation.setMainToolbarWithNavigation(toolbarName);
    }

    private void setupRecipePicker() {
        // observe all recipes
        mRecipesAdapter = new AutoCompleteRecipeAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line);
        mModel.getAllRecipes().observe(mActivity, recipes -> mRecipesAdapter.setData(recipes));

        mRecipeName.setAdapter(mRecipesAdapter);

        // update selected meal on uer click
        mRecipeName.setOnItemClickListener((parent, view, position, id) -> {
            IRecipe selectedRecipe = (IRecipe) parent.getItemAtPosition(position);
            mMeal.setImage_url(selectedRecipe.getImage_url());
            mMeal.setTitle(selectedRecipe.getTitle());
        });
    }

    private void setupGroupMemberPicker() {
        UserAdapter usersSpinnerAdapter = new UserAdapter(mActivity, R.layout.group_member_item, R.id.userName);
        mGroupMembers.setAdapter(usersSpinnerAdapter);
        mGroupMembers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String userId = parent.getItemAtPosition(position).toString();
                Map<String, String> theUser = null;
                for(Map<String, String> user: mUserGroup.getAllUsers()) {
                    if(user.get("id").equals(userId)) {
                        theUser = user;
                        break;
                    }
                }
                if(theUser != null) {
                    mMeal.setCookName(getFirstWord(theUser.get("name")));
                    mMeal.setCookImage(theUser.get("photo_url"));
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // observe group members for changes
        mAccount.getUserGroup().observe(mActivity, group -> {
            mUserGroup = group;
            usersSpinnerAdapter.setData(group.getAllUsers());
            if(mMeal != null && mMeal.getCookName() != null) mGroupMembers.setSelection(usersSpinnerAdapter.getPosition(getFirstWord(mMeal.getCookName())));
        });
    }

    private void deletingHandler() {

        mModel.deleteSelectedMeal();
        Toast.makeText(getActivity(), R.string.delete_meal_message, Toast.LENGTH_LONG).show();
        mNavigation.getNavController().navigateUp();
    }

    private void savingHandler() {

        if (mMeal.getDate().compareTo(new Date()) < 0) {
            Toast.makeText(getActivity(), R.string.add_meal_is_in_past_error, Toast.LENGTH_LONG).show();
        } else {
            mModel.selectMeal(mMeal);
            mModel.createMeal();
            Toast.makeText(getActivity(), mIsEditMode ? R.string.add_meal_update_success : R.string.add_meal_save_success, Toast.LENGTH_LONG).show();
            mNavigation.getNavController().navigateUp();
        }

    }

    private void showTimePickerDialog() {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(mFragmentManager,TIME_PICKER_TAG);
    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(mFragmentManager, DATE_PICKER_TAG);
    }

    private void updateView() {

        SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, d. MMM", Locale.US);

        mRecipeName.setText(mMeal.getTitle());
        mMealDate.setText(sdf.format(mMeal.getDate()));
        mMealTime.setText(DateUtils.formatDateTime(mActivity, mMeal.getDate().getTime(), DateUtils.FORMAT_SHOW_TIME));
    }

    /* Adapted from http://android-er.blogspot.com/2010/12/custom-arrayadapter-for-spinner-with.html */
    private class UserAdapter extends ArrayAdapter<String> {
        private List<Map<String, String>> mUsers;
        private List<String> mUsernames;

        UserAdapter(@NonNull Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        public void setData(List<Map<String, String>> users) {
            mUsers = users;
            mUsernames = new ArrayList<>();
            for(Map<String, String> user : mUsers) {
                mUsernames.add(getFirstWord(Objects.requireNonNull(user.get("id"))));
            }
            clear();
            addAll(mUsernames);
            notifyDataSetChanged();
        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    @NonNull ViewGroup parent) {
            return getCustomView(position, parent);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            return getCustomView(position, parent);
        }

        View getCustomView(int position, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.group_member_item, parent, false);

            TextView userName = row.findViewById(R.id.userName);
            ImageView userPhoto = row.findViewById(R.id.userPhoto);

            String photoUrl = mUsers.get(position).get("photo_url");
            userName.setText(getFirstWord(Objects.requireNonNull(mUsers.get(position).get("name"))));

            GlideApp.with(row)
                    .load(photoUrl)
                    .placeholder(R.drawable.crispy_icon)
                    .circleCrop()
                    .into(userPhoto);

            return row;
        }

    }

    /* From https://stackoverflow.com/a/17008136 */
    private String getFirstWord(String text) {
        int index = text.indexOf(' ');
        if (index > -1) { // Check if there is more than one word.
            return text.substring(0, index); // Extract first word.
        } else {
            return text; // Text is the first word itself.
        }
    }

}



