package team.returnteamname.servicenovigrad.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import team.returnteamname.servicenovigrad.R;

public class ServiceActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);

        Button driverLicenseButton, healthCardButton, photoIdButton;

        driverLicenseButton = findViewById(R.id.buttonDriverLicense);
        healthCardButton    = findViewById(R.id.buttonHealthCard);
        photoIdButton       = findViewById(R.id.buttonPhotoID);
        

    }
}