package team.returnteamname.servicenovigrad.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

import team.returnteamname.servicenovigrad.R;

public class DriverLicenseActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverlicense);

        EditText[] editTextValues = new EditText[8];

        editTextValues[0] = findViewById(R.id.editTextFirstName);
        editTextValues[1] = findViewById(R.id.editTextLastName);
        editTextValues[2] = findViewById(R.id.editTextDateofBirth);
        editTextValues[3] = findViewById(R.id.editTextAddress1);
        editTextValues[4] = findViewById(R.id.editTextAddress2);
        editTextValues[5] = findViewById(R.id.editTextCity);
        editTextValues[6] = findViewById(R.id.editTextProvince);
        editTextValues[7] = findViewById(R.id.editTextPostalAddress);
    }
}