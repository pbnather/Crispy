package dk.au.itsmap.group4.crispy.ui.account;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.lifecycle.ViewModel;

public class AccountViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    public FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }
}
