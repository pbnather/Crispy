package dk.au.itsmap.group4.crispy.ui.mealsPlan.addPlannedMeal;

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
import dk.au.itsmap.group4.crispy.ui.MainNavigationActivity;
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
    private MainNavigationActivity mActivity;
    private TextView mMealDate, mMealTime;
    private Spinner mGroupMembers;
    private View mView;

    private AutoCompleteRecipeAdapter mRecipesAdapter;
    private AutoCompleteTextView mRecipeName;
    private IRecipe mSelectedRecipe;

    private boolean mIsEditMode = false;
    private Map<String, String>  mSelectedUser;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity = (MainNavigationActivity) this.getActivity();
        mAccount = mActivity;
        mNavigation = mActivity;
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
        mModel.getSelectedMeal().observe(this, meal -> {
            IMeal lastMeal = mMeal;
            mMeal = meal;
            updateView(lastMeal);
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

        mActivity.setMainToolbarWithNavigation(toolbarName);
    }

    private void setupRecipePicker() {
        // observe all recipes
        mRecipesAdapter = new AutoCompleteRecipeAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line);
        mModel.getAllRecipes().observe(this, recipes -> mRecipesAdapter.setData(recipes));

        mRecipeName.setAdapter(mRecipesAdapter);

        // update selected meal on uer click
        mRecipeName.setOnItemClickListener((parent, view, position, id) -> mSelectedRecipe = (IRecipe) parent.getItemAtPosition(position));
    }

    private void setupGroupMemberPicker() {
        UserAdapter usersSpinnerAdapter = new UserAdapter(mActivity, R.layout.group_member_item, R.id.userName);
        mGroupMembers.setAdapter(usersSpinnerAdapter);
        mGroupMembers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String userId = parent.getItemAtPosition(position).toString();
                for(Map<String, String> user: mUserGroup.getAllUsers()) {
                    if(Objects.requireNonNull(user.get("id")).equals(userId)) {
                        mSelectedUser = user;
                        break;
                    }
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // observe group members for changes
        mAccount.getUserGroup().observe(this, group -> {
            mUserGroup = group;
            usersSpinnerAdapter.setData(group.getAllUsers());
            if(mMeal != null && mMeal.getCookName() != null) {
                String name = mMeal.getCookName();
                for(Map<String, String> user: group.getAllUsers()) {
                    if(getFirstWord(Objects.requireNonNull(user.get("name"))).equals(name)) {
                        mSelectedUser = user;
                        break;
                    }
                }
                mGroupMembers.setSelection(usersSpinnerAdapter.getPosition(mSelectedUser.get("id")));
            }
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
            if (mSelectedRecipe == null) {
                mMeal.setTitle(mRecipeName.getText().toString());
                mMeal.setImage_url(null);
                mMeal.setRecipeId(null);
            } else {
                if(mSelectedRecipe.getTitle().equals(mRecipeName.getText().toString())) {
                    mMeal.setTitle(mSelectedRecipe.getTitle());
                    mMeal.setImage_url(mSelectedRecipe.getImage_url());
                    mMeal.setRecipeId(mSelectedRecipe.getId());
                } else {
                    mMeal.setTitle(mRecipeName.getText().toString());
                    mMeal.setImage_url(null);
                    mMeal.setRecipeId(null);
                }
            }
            if(mSelectedUser != null) {
                mMeal.setCookName(getFirstWord(Objects.requireNonNull(mSelectedUser.get("name"))));
                mMeal.setCookImage(mSelectedUser.get("photo_url"));
            }
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

    private void updateView(IMeal lastMeal) {
        SimpleDateFormat sdf = new java.text.SimpleDateFormat("EEEE, d. MMM", Locale.US);

        if(lastMeal == null) {
            mRecipeName.setText(mMeal.getTitle());
        } else if(lastMeal.getTitle() != null && !lastMeal.getTitle().equals(mMeal.getTitle())) {
            mRecipeName.setText(mMeal.getTitle());
        }
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



