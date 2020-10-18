package team.returnteamname.servicenovigrad.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import team.returnteamname.servicenovigrad.R;

public class WelcomeActivity extends AppCompatActivity
{
    private TextView welcomeMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        String firstName = getIntent().getStringExtra("firstName");
        welcomeMessage = (TextView) findViewById(R.id.textView);
        welcomeMessage.setText("Welcome"+firstName);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }
}