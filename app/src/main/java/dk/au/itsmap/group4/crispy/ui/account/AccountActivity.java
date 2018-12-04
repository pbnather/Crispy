package dk.au.itsmap.group4.crispy.ui.account;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import dk.au.itsmap.group4.crispy.R;
import dk.au.itsmap.group4.crispy.ui.CrispyAuthenticatedActivity;

public class AccountActivity extends CrispyAuthenticatedActivity {

    Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);

        mToolbar = findViewById(R.id.mainToolbar);
        mToolbar.setTitle(R.string.crispy_planner);
        setSupportActionBar(mToolbar);
    }
}
