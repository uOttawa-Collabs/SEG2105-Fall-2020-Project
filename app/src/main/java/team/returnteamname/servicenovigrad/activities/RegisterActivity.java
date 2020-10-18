package team.returnteamname.servicenovigrad.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import team.returnteamname.servicenovigrad.R;

public class RegisterActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // onclick event
        findViewById(R.id.buttonRegisterSubmit).setOnClickListener(
            v -> startActivityForResult(new Intent(getApplicationContext(), WelcomeActivity.class),
                                        0));
    }
}
