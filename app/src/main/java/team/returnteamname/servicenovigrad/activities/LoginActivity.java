package team.returnteamname.servicenovigrad.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.AccountManager;
import team.returnteamname.servicenovigrad.account.UserAccount;

public class LoginActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText editTextUsername = findViewById(R.id.editTextUsername);
        EditText editTextPassword = findViewById(R.id.editTextPassword);

        Button loginButton = findViewById(R.id.buttonLogin);

        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String         username       = editTextUsername.getText().toString().trim();
                String         password       = editTextPassword.getText().toString().trim();
                AccountManager accountManager = AccountManager.getInstance();

                if (!accountManager.isInitialized())
                {
                    Toast.makeText(getApplicationContext(),
                                   "Database is not initialized, please check your connection",
                                   Toast.LENGTH_SHORT).show();
                }
                else if (username.equals("") || password.equals(""))
                {
                    Toast.makeText(getApplicationContext(), "Username and password cannot be empty",
                                   Toast.LENGTH_SHORT).show();
                }
                else
                {
                    UserAccount account = new UserAccount(username, password, null, null, null,
                                                          null);

                    if (accountManager.verifyAccount(account))
                    {
                        Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
                        intent.putExtra("verifiedAccount", account);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Username and password mismatch",
                                       Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
