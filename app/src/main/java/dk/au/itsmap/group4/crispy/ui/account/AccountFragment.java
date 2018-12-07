package dk.au.itsmap.group4.crispy.ui.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

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
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IUserGroup;
import dk.au.itsmap.group4.crispy.service.GlideApp;
import dk.au.itsmap.group4.crispy.ui.MainNavigationActivity;
import dk.au.itsmap.group4.crispy.ui.IAccountManager;

public class AccountFragment extends Fragment {

    private IAccountManager mAccount;
    private MainNavigationActivity mActivity;

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
        View rootView = inflater.inflate(R.layout.account_fragment, container, false);

        // Set sign out button action
        rootView.findViewById(R.id.signOutBtn).setOnClickListener(button -> mAccount.signOut());

        // Set toolbar
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        mActivity = (MainNavigationActivity) this.getActivity();
        mActivity.setMainToolbarWithNavigation("Your profile");

        // Set profile image
        ImageView profilePicture = rootView.findViewById(R.id.profilePicture);
        GlideApp.with(this)
                .load(mAccount.getUserPhotoUrl())
                .placeholder(R.drawable.default_profile_picture_hd)
                .into(profilePicture);

        // Set welcome text
        TextView accountNameText = rootView.findViewById(R.id.accountNameText);
        accountNameText.setText(String.format("Hi %s", mAccount.getUserName()));
        rootView.findViewById(R.id.signOutBtn).setOnClickListener(button -> {
            mAccount.signOut();
            mActivity.getNavController().popBackStack();
        });

        // Set list of group members
        mAccount.getUserGroup().observe(this, this::displayGroupMembers);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    private void displayGroupMembers(IUserGroup group) {

    }
}
