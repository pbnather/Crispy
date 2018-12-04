package dk.au.itsmap.group4.crispy.ui;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.transition.Transition;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.database.FSRepository;
import dk.au.itsmap.group4.crispy.model.IUserGroup;
import dk.au.itsmap.group4.crispy.service.GlideApp;

public abstract class CrispyAuthenticatedActivity extends AppCompatActivity {

    private static final String TAG = "AuthActivity";
    private static final int RC_SIGN_IN = 451;

    private FirebaseAuth mAuth;
    protected Menu mMenu;

    protected FirebaseUser mCurrentUser;
    protected IUserGroup mUserGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        authenticateUser();
    }

    private void authenticateUser() {
        mCurrentUser = mAuth.getCurrentUser();
        if(mCurrentUser == null) {
            signIn();
        } else if(mUserGroup == null)
            FSRepository
                .getInstance()
                .getUserGroup(mCurrentUser.getUid())
                .observe(this, group -> mUserGroup = group);
    }

    private void updateUserProfile(String username, String photoUrl) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(username)
                .setPhotoUri(Uri.parse(photoUrl))
                .build();

        if(mCurrentUser != null) {
            mCurrentUser.updateProfile(profileUpdates)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            updateMenu();
                        }
                    });
        }
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
                        .build(), RC_SIGN_IN);
    }

    protected void signOut() {
        AuthUI.getInstance()
                .signOut(this).addOnSuccessListener(task -> {
                authenticateUser();
                });
    }

    private void registerUser(String userId, String username) {
        FSRepository
                .getInstance()
                .createUserWithGroup(userId, username);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        mMenu = menu;
        updateMenu();
        return true;
    }

    /* Adapted from https://stackoverflow.com/a/37116931 */
    private void updateMenu() {
        GlideApp.with(this)
                .load(mCurrentUser != null ? mCurrentUser.getPhotoUrl() : null)
                //.placeholder(ContextCompat.getDrawable(this, R.drawable.default_profile_picture))
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onStop() {

                    }

                    @Override
                    public void onDestroy() {

                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        mMenu.findItem(R.id.btnAccount).setIcon(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void removeCallback(@NonNull SizeReadyCallback cb) {

                    }

                    @Override
                    public void setRequest(@Nullable Request request) {

                    }

                    @Nullable
                    @Override
                    public Request getRequest() {
                        return null;
                    }

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {

                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        //mMenu.findItem(R.id.btnAccount).setIcon(errorDrawable);
                    }
                });
    }

    @Override
    /* Adapted from https://developer.android.com/training/appbar/actions */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnGroceryList:
                //TODO: navigate to grocery list instead of signing out :)
                if (mCurrentUser != null) {
                    signOut();
                }
                return true;
            case R.id.btnAccount:
                // TODO: navigate to AccountFragment using Navigation
                getNavController().navigate(R.id.accountFragment);

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            // Successfully signed in
            if (resultCode == RESULT_OK) {
                authenticateUser();
                updateMenu();
                if (response != null && response.isNewUser()) {
                    registerUser(mCurrentUser.getUid(), mCurrentUser.getDisplayName());
                }
            } else {
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    // TODO: Notify user about no internet connection
                    return;
                } else {
                    authenticateUser();
                    updateMenu();
                }

                // TODO: Notify user about unknown error
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }

    abstract protected NavController getNavController();
}
