package team.returnteamname.servicenovigrad.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import team.returnteamname.servicenovigrad.R;

public class MainActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Login button onClick event binding
        findViewById(R.id.buttonLogin).setOnClickListener(
            v -> startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class),
                                        0));

        // Register button onClick event binding
        findViewById(R.id.buttonCreateAnAccount).setOnClickListener(v -> startActivityForResult(
            new Intent(getApplicationContext(), RegisterActivity.class), 0));

    }
}
