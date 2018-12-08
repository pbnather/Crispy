package dk.au.itsmap.group4.crispy.ui.account;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IIngredient;
import dk.au.itsmap.group4.crispy.model.IUserGroup;
import dk.au.itsmap.group4.crispy.utils.GlideApp;
import dk.au.itsmap.group4.crispy.ui.MainNavigationActivity;
import dk.au.itsmap.group4.crispy.ui.IAccountManager;

public class AccountFragment extends Fragment {

    private IAccountManager mAccount;
    private MainNavigationActivity mActivity;
    private TableLayout groupTable;
    private View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mAccount = (IAccountManager) getActivity();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem account =menu.findItem(R.id.btnAccount);
        MenuItem list = menu.findItem(R.id.btnGroceryList);
        account.setVisible(false);
        list.setVisible(false);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.account_fragment, container, false);

        // Set sign out button action
        rootView.findViewById(R.id.signOutBtn).setOnClickListener(button -> mAccount.signOut());

        // Set toolbar
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mActivity = (MainNavigationActivity) this.getActivity();
        mActivity.setMainToolbarWithNavigation(getText(R.string.account).toString());

        // Set profile image
        ImageView profilePicture = rootView.findViewById(R.id.profilePicture);
        GlideApp.with(this)
                .load(mAccount.getUserPhotoUrl())
                .placeholder(R.drawable.default_profile_picture_hd)
                .circleCrop()
                .into(profilePicture);

        // Set welcome text
        TextView accountNameText = rootView.findViewById(R.id.accountNameText);
        accountNameText.setText(String.format(getText(R.string.hi).toString()+" %s!", mAccount.getUserName()));

        //Set sign out
        rootView.findViewById(R.id.signOutBtn).setOnClickListener(button -> {
            mAccount.signOut();
            mActivity.getNavController().popBackStack();
        });

        // Set list of group members
        groupTable = rootView.findViewById(R.id.groupMembersList);
        mAccount.getUserGroup().observe(this, (group) -> displayGroupMembers(group, inflater, container));

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    private void displayGroupMembers(IUserGroup group, LayoutInflater inflater, ViewGroup container) {
        groupTable.removeAllViews();

        TextView groupName = rootView.findViewById(R.id.groupName);
        groupName.setText(group.getName()+":");

        for(int i=0; i<group.getAllUsers().size(); i++) {
            // add array of views

            View userRowLayout = inflater.inflate(R.layout.group_member_item, container, false);

            TextView nameView = userRowLayout.findViewById(R.id.userName);
            ImageView pictureView = userRowLayout.findViewById(R.id.userPhoto);

            String userName = group.getAllUsers().get(i).get("name");
            String pictureUrl = group.getAllUsers().get(i).get("photo_url");

            GlideApp.with(userRowLayout)
                    .load(pictureUrl)
                    .placeholder(R.drawable.crispy_icon)
                    .circleCrop()
                    .into(pictureView);

            nameView.setText(userName);
            groupTable.addView(userRowLayout);
        }
    }
}
