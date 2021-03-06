package dk.au.itsmap.group4.crispy.ui;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import dk.au.itsmap.group4.crispy.database.FSRepository;
import dk.au.itsmap.group4.crispy.model.IUserGroup;

public class AuthViewModel extends AndroidViewModel {

    private final static String TAG = "AuthActivity";

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();;
    private final FirebaseAuthLiveData mUser = new FirebaseAuthLiveData();
    private final FSRepository mRepository = FSRepository.getInstance();

    public AuthViewModel(@NonNull Application application) {
        super(application);
    }

    public FirebaseAuthLiveData getCurrentUser() {
        return mUser;
    }

    public LiveData<List<IUserGroup>> getUserGroup(String userId) {
        return mRepository.getUserGroup(userId);
    }

    public void registerUser() {
        FirebaseUser user = mUser.getValue();
        if(user != null) {
            String userId = mUser.getValue().getUid();
            String userName = mUser.getValue().getDisplayName();
            mRepository.createUserAndAssignDefaultGroup(userId, userName == null ? "" : userName, user.getPhotoUrl() == null ? "" : user.getPhotoUrl().toString());
        }
    }

    public void deleteUser(Context context) {
        String userId = mUser.getValue().getUid();
        if(userId == null) return;
        mRepository.getUserGroup(userId).observe((AuthActivity)context, group -> {
            if(group != null) {
                AuthUI.getInstance().delete(context).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Deletion succeeded
                        if(!group.isEmpty()) mRepository.deleteUser(userId, group.get(0));
                        ((AuthActivity) context).signIn();
                    } else {
                        // Deletion failed
                        Log.w(TAG, "Deleting user failed :c");
                        AuthUI.getInstance().signOut(context);
                    }
                });
            }
        });
    }

    /* Adapted from https://stackoverflow.com/q/50035915 */
    private class FirebaseAuthLiveData extends LiveData<FirebaseUser> {
        private FirebaseAuth.AuthStateListener authStateListener =
                firebaseAuth -> {
                    FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                    setValue(firebaseUser);   };

        @Override
        protected void onActive() {
            super.onActive();
            mAuth.addAuthStateListener(authStateListener);
        }

        @Override
        protected void onInactive() {
            super.onInactive();
            mAuth.removeAuthStateListener(authStateListener);
        }
    }
}
