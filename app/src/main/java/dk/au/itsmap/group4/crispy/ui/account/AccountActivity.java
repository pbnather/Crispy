package dk.au.itsmap.group4.crispy.ui.account;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import dk.au.itsmap.group4.crispy.R;

public class AccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, AccountFragment.newInstance())
                    .commitNow();
        }
    }
}
