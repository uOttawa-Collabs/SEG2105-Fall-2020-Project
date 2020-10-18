package team.returnteamname.servicenovigrad.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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


        EditText[] editTextValues = new EditText[6];

        editTextValues[0] = (EditText) findViewById(R.id.editTextFirstName);
        editTextValues[1] = (EditText) findViewById(R.id.editTextLastName);
        editTextValues[2]  = (EditText) findViewById(R.id.editTextEmail);
        editTextValues[3] = (EditText) findViewById(R.id.editTextUsername);
        editTextValues[4] = (EditText) findViewById(R.id.editTextPassword);
        editTextValues[5] = (EditText) findViewById(R.id.editTextPasswordConfirm);

        String   firstName  = editTextValues[0].getText().toString();
        String   lastName  = editTextValues[1] .getText().toString();
        String   userEmail  = editTextValues[2].getText().toString();
        String   username  = editTextValues[3].getText().toString();
        String   userPassword  = editTextValues[4].getText().toString();
        String   userPassword2  = editTextValues[5].getText().toString();
    }
}
