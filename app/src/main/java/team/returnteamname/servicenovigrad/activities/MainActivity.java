package team.returnteamname.servicenovigrad.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.AccountManager;

public class MainActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton, registerButton;

        loginButton    = findViewById(R.id.buttonLogin);
        registerButton = findViewById(R.id.buttonCreateAnAccount);

        // Login button onClick event binding
        loginButton.setOnClickListener(
            v -> startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class),
                                        0));

        // Register button onClick event binding
        registerButton.setOnClickListener(v -> startActivityForResult(
            new Intent(getApplicationContext(), RegisterActivity.class), 0));

        // Initialize AccountManager
        AccountManager accountManager = AccountManager.getInstance();
        accountManager.initialize();

        // Set buttons to enable after initialization
        accountManager.addAccountManagerCallback("enableMainActivityButtons",
                                                 () ->
                                                 {
                                                     loginButton.setEnabled(true);
                                                     registerButton.setEnabled(true);
                                                 });


    }
}
