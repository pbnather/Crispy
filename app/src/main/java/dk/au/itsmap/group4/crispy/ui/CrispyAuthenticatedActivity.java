package dk.au.itsmap.group4.crispy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import dk.au.itsmap.group4.crispy.R;

public abstract class CrispyAuthenticatedActivity extends AppCompatActivity {

    private static final String TAG = "AuthenticatedActivity";
    private static final int RC_SIGN_IN = 451;

    protected FirebaseUser mCurrentUser;
    protected FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        checkUserLoggedIn();
    }

    private void checkUserLoggedIn() {
        if(mCurrentUser == null) signIn();
    }

    private void signIn() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.Crispy)
                        .build(),
                RC_SIGN_IN);
    }

    protected void signOut() {
        AuthUI.getInstance()
                .signOut(this).addOnSuccessListener(task -> signIn());
    }

    private void registerUser() {
        // TODO: Add user to the database and to the group
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    // Adapted from https://developer.android.com/training/appbar/actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.btnGroceryList:
                //TODO: navigate to grocery list
                if (mCurrentUser != null) {
                    signOut();
                }
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
                if (response != null && response.isNewUser()) {
                    registerUser();
                }
            } else {
                // Sign in failed
                if (response == null) {
                    // User pressed back button
                    signIn();
                    return;
                }

                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    // TODO: Notify user about no internet connection
                    return;
                }

                // TODO: Notify user about unknown error
                Log.e(TAG, "Sign-in error: ", response.getError());
            }
        }
    }
}
