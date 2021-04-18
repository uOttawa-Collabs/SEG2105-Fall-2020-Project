package team.returnteamname.servicenovigrad.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.Account;
import team.returnteamname.servicenovigrad.manager.AccountManager;

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

        loginButton.setOnClickListener(
            v ->
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
                    Account account = new Account(username, password, null);

                    if ((account = accountManager.verifyAccount(account)) != null)
                    {
                        Intent intent = new Intent(getApplicationContext(),
                                                   DashboardActivity.class);
                        intent.putExtra("account", account);
                        startActivity(intent);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Username and password mismatch",
                                       Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}
