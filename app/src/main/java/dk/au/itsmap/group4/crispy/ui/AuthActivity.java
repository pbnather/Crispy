package dk.au.itsmap.group4.crispy.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProviders;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IUserGroup;
import dk.au.itsmap.group4.crispy.service.GlideApp;

public abstract class AuthActivity extends AppCompatActivity implements INavigationActivity, IAccountManager {

    private static final String TAG = "AuthActivity";
    private static final int RC_SIGN_IN = 451;

    protected Menu mMenu;
    private AuthViewModel mAuth;
    private FirebaseUser mUser;
    private LiveData<List<IUserGroup>> mUserGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = ViewModelProviders.of(this).get(AuthViewModel.class);

        mUserGroup = new LiveData<List<IUserGroup>>() {};
        LiveData<FirebaseUser> firebaseUser = mAuth.getCurrentUser();
        firebaseUser.observeForever(user -> {
            if (user == null) {
                stopObservingUserGroup();
                signIn();
            } else {
                mUser = user;
                observeUserGroup();
            }
            updateMenu(user);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                updateMenu(mUser);
                if (response != null && response.isNewUser()) {
                    mAuth.registerUser();
                }
            } else {
                if (response == null) {
                    signIn();
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    // TODO: Notify user about no internet connection
                } else {
                    // TODO: Notify user about unknown error
                    Log.e(TAG, "Sign-in error: ", response.getError());
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        mMenu = menu;
        updateMenu(mUser);
        return true;
    }

    @Override
    /* Adapted from https://developer.android.com/training/appbar/actions */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnGroceryList:
                // TODO: go to grocery store
                return true;
            case R.id.btnAccount:
                getNavController().navigate(R.id.accountFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /* Adapted from https://stackoverflow.com/a/37116931 */
    private void updateMenu(FirebaseUser user) {
        if(mMenu == null) return;
        GlideApp.with(this)
                .asDrawable()
                .load(user != null ? user.getPhotoUrl() : null)
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        mMenu.findItem(R.id.btnAccount).setIcon(resource);
                    }});
    }

    private void signIn() {
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.Crispy)
                        .setLogo(R.mipmap.new_crispy_launcher_round)
                        .build(), RC_SIGN_IN);
    }

    @Override
    public void signOut() {
        AuthUI.getInstance()
                .signOut(this);
    }

    @Override
    public LiveData<IUserGroup> getUserGroup() {
        return Transformations.map(mUserGroup, userGroup -> userGroup == null ? null : userGroup.get(0));
    }

    private void observeUserGroup() {
        mUserGroup = mAuth.getUserGroup(mUser.getUid());
    }

    private void stopObservingUserGroup() {
        mUserGroup = new LiveData<List<IUserGroup>>() {};
    }
}
