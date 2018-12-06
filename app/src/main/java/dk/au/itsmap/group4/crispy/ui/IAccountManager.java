package dk.au.itsmap.group4.crispy.ui;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import dk.au.itsmap.group4.crispy.model.IUserGroup;

public interface IAccountManager {

    void signOut();

    LiveData<IUserGroup> getUserGroup();

    Uri getUserPhotoUrl();

    String getUserName();
}
