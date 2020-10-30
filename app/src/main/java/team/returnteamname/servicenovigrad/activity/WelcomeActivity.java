package team.returnteamname.servicenovigrad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.Account;
import team.returnteamname.servicenovigrad.account.UserAccount;
import team.returnteamname.servicenovigrad.manager.AccountManager;

public class WelcomeActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        TextView textView = findViewById(R.id.textView);

        if (bundle != null)
        {
            Account        account        = (Account) bundle.get("verifiedAccount");
            AccountManager accountManager = AccountManager.getInstance();

            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("Welcome");

            if (!account.getRole().equals("Administrator"))
            {
                stringBuilder.append(' ').append(((UserAccount) account).getFirstName());
            }

            stringBuilder.append("! You are logged in as ").append(account.getRole()).append('.');

            textView.setText(stringBuilder);
        }
        else
            throw new IllegalArgumentException("Received an intent with no argument");
    }
}
