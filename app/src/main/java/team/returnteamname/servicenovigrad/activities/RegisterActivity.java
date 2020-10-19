package team.returnteamname.servicenovigrad.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import team.returnteamname.servicenovigrad.R;

public class RegisterActivity extends Activity
{
    EditText[] editTextValues = new EditText[6];
    Intent intent;
    String   firstName,lastName,userEmail,username,userPassword,userPassword2;
    Spinner spinnerRole;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Spinner spinner = findViewById(R.id.spinnerRoleSelector);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                                                                             R.array.rolesRegister,
                                                                             android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        editTextValues[0] = findViewById(R.id.editTextFirstName);
        editTextValues[1] = findViewById(R.id.editTextLastName);
        editTextValues[2] = findViewById(R.id.editTextEmail);
        editTextValues[3] = findViewById(R.id.editTextUsername);
        editTextValues[4] = findViewById(R.id.editTextPassword);
        editTextValues[5] = findViewById(R.id.editTextPasswordConfirm);

        firstName     = editTextValues[0].getText().toString();
        lastName      = editTextValues[1].getText().toString();
        userEmail     = editTextValues[2].getText().toString();
        username      = editTextValues[3].getText().toString();
        userPassword  = editTextValues[4].getText().toString();
        userPassword2 = editTextValues[5].getText().toString();

        spinnerRole = (Spinner) findViewById(R.id.spinnerRoleSelector);
        spinnerRole.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) this);

        intent = new Intent(this, WelcomeActivity.class);
        intent.putExtra("firstName", firstName);
        startActivity(intent);


        // onclick event, by click submit, pass first name to the welcome page
        findViewById(R.id.buttonRegisterSubmit).setOnClickListener(

            v -> startActivityForResult(new Intent(getApplicationContext(), WelcomeActivity.class),
                                        0));
    }
}
