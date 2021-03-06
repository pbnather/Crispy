package dk.au.itsmap.group4.crispy.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModelProviders;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.model.IUserGroup;
import dk.au.itsmap.group4.crispy.utils.GlideApp;

public abstract class AuthActivity extends AppCompatActivity implements INavigationController, IAccountManager {

    private static final String TAG = "AuthActivity";
    private static final int RC_SIGN_IN = 451;

    protected Menu mMenu;
    private AuthViewModel mAuth;
    private FirebaseUser mUser;
    private LiveData<List<IUserGroup>> mUserGroup;
    private boolean mIsInteractingWithAccount = false;
    private boolean mIsSigningIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = ViewModelProviders.of(this).get(AuthViewModel.class);
        mIsSigningIn = false;
        mUserGroup = new LiveData<List<IUserGroup>>() {};
        LiveData<FirebaseUser> firebaseUser = mAuth.getCurrentUser();
        firebaseUser.observeForever(user -> {
            mUser = user;
                if (user == null) {
                    stopObservingUserGroup();
                    if(!mIsSigningIn) signIn();
                } else {
                    observeUserGroup();
                    mIsInteractingWithAccount = false;
                }
                updateMenu();
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mIsInteractingWithAccount = false;
        mIsSigningIn = false;
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                updateMenu();
                if (response != null && response.isNewUser()) {
                    mAuth.registerUser();
                }
                getNavController().navigateUp();
            } else {
                if (response == null)
                {
                    signIn();
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, getString(R.string.unknown_error), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Sign-in error: ", response.getError());
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        mMenu = menu;
        updateMenu();
        return true;
    }

    @Override
    /* Adapted from https://developer.android.com/training/appbar/actions */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnGroceryList:
                // go to grocery store
                return true;
            case R.id.btnAccount:
                getNavController().navigate(R.id.accountFragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    /* Adapted from https://stackoverflow.com/a/37116931 */
    private void updateMenu() {
        if(mMenu == null) return;
        if(mIsInteractingWithAccount || mIsSigningIn) return;
        GlideApp.with(this)
                .asDrawable()
                .load(getUserPhotoUrl())
                .circleCrop()
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        mMenu.findItem(R.id.btnAccount).setIcon(resource);
                    }});
    }

    public void signIn() {
        if (!mIsSigningIn) {
            mIsSigningIn = true;
            mIsInteractingWithAccount = true;
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setTheme(R.style.Crispy)
                            .setLogo(R.drawable.crispy_icon)
                            .build(), RC_SIGN_IN);
        }
    }

    @Override
    public void signOut() {
        mIsInteractingWithAccount = true;
        AuthUI.getInstance()
                .signOut(this);
    }

    @Override
    public LiveData<IUserGroup> getUserGroup() {
        return Transformations.map(mUserGroup, userGroup -> userGroup == null || userGroup.isEmpty() ? null : userGroup.get(0));
    }

    @Override
    public Uri getUserPhotoUrl() {
        return mUser != null ? mUser.getPhotoUrl() : null;
    }

    @Override
    public String getUserName() {
        return mUser != null ? mUser.getDisplayName() : null;
    }

    @Override
    public String getUserId() {
        return mUser != null ? mUser.getUid() : null;
    }

    @Override
    public void deleteUser() {
        mIsInteractingWithAccount = true;
        mAuth.deleteUser(this);
    }

    private void observeUserGroup() {
        mUserGroup = mAuth.getUserGroup(mUser.getUid());
    }

    private void stopObservingUserGroup() {
        mUserGroup = new LiveData<List<IUserGroup>>() {};
    }
}
