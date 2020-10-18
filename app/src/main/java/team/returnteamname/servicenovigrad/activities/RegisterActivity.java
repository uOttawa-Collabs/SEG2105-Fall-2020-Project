package team.returnteamname.servicenovigrad.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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

        Spinner spinner = (Spinner) findViewById(R.id.spinnerRoleSelector);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                                                                             R.array.rolesRegister, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
    }
}
