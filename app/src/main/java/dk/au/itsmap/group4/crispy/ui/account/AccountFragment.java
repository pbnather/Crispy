package dk.au.itsmap.group4.crispy.ui.account;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.service.GlideApp;

public class AccountFragment extends Fragment {

    private AccountViewModel mViewModel;
    private FirebaseUser mCurrentUser;
    private Activity mActivity;

    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mViewModel = ViewModelProviders.of(this).get(AccountViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mActivity = this.getActivity();
        View rootView = inflater.inflate(R.layout.account_fragment, container, false);

        Toolbar toolbar = (Toolbar) mActivity.findViewById(R.id.mainToolbar);
        toolbar.setTitle("Your profile");
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        ImageView profilePicture = rootView.findViewById(R.id.profilePicture);
        mCurrentUser = mViewModel.getCurrentUser();
        GlideApp.with(this)
                .load(mCurrentUser != null ? mCurrentUser.getPhotoUrl() : null)
                .placeholder(R.drawable.default_profile_picture_hd)
                .into(profilePicture);
        TextView accountNameText = rootView.findViewById(R.id.accountNameText);
        accountNameText.setText(String.format("Hi %s", mCurrentUser.getDisplayName()));
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

}
