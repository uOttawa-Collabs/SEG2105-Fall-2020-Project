package team.returnteamname.servicenovigrad.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import team.returnteamname.servicenovigrad.R;

public class LoginActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //onclick event
        findViewById(R.id.buttonLogin).setOnClickListener(
            v -> startActivityForResult(new Intent(getApplicationContext(), WelcomeActivity.class),
                                        0));
    }
}
