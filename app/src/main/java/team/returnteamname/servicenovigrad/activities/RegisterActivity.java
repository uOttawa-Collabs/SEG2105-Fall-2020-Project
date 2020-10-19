package team.returnteamname.servicenovigrad.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.AccountManager;
import team.returnteamname.servicenovigrad.account.CustomerAccount;
import team.returnteamname.servicenovigrad.account.EmployeeAccount;
import team.returnteamname.servicenovigrad.account.UserAccount;

public class RegisterActivity extends Activity
{
    Integer selectedRoleIndex = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText[] editTextValues = new EditText[6];

        editTextValues[0] = findViewById(R.id.editTextFirstName);
        editTextValues[1] = findViewById(R.id.editTextLastName);
        editTextValues[2] = findViewById(R.id.editTextEmail);
        editTextValues[3] = findViewById(R.id.editTextUsername);
        editTextValues[4] = findViewById(R.id.editTextPassword);
        editTextValues[5] = findViewById(R.id.editTextPasswordConfirm);

        AccountManager accountManager = AccountManager.getInstance();

        Button button = findViewById(R.id.buttonRegisterSubmit);

        ArrayList<String> availableRoles = accountManager.getAvailableRoles();

        Spinner spinner = findViewById(R.id.spinnerRoleSelector);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                                                          R.layout.spinner_layout,
                                                          availableRoles.subList(1,
                                                                                 availableRoles.size()));

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                setSelectedRole(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                setSelectedRole(null);
            }
        });

        button.setOnClickListener(v ->
                                  {
                                      String firstName       = editTextValues[0].getText().toString().trim();
                                      String lastName        = editTextValues[1].getText().toString().trim();
                                      String email           = editTextValues[2].getText().toString().trim();
                                      String username        = editTextValues[3].getText().toString().trim();
                                      String password        = editTextValues[4].getText().toString().trim();
                                      String passwordConfirm = editTextValues[5].getText().toString().trim();

                                      if (!accountManager.isInitialized())
                                      {
                                          Toast.makeText(getApplicationContext(),
                                                         "Database is not initialized, please check your connection",
                                                         Toast.LENGTH_SHORT).show();
                                      }
                                      else if (firstName.equals("")
                                               || lastName.equals("")
                                               || email.equals("")
                                               || username.equals("")
                                               || password.equals("")
                                               || passwordConfirm.equals(""))
                                      {
                                          Toast.makeText(getApplicationContext(),
                                                         "All fields are required",
                                                         Toast.LENGTH_SHORT).show();
                                      }
                                      else if (!email.matches("^[\\w-\\.]+@[\\w-]+\\.+[\\w-]{2,}$"))
                                      {
                                          Toast.makeText(getApplicationContext(),
                                                         "Invalid email address",
                                                         Toast.LENGTH_SHORT).show();
                                      }
                                      else if (!password.equals(passwordConfirm))
                                      {
                                          Toast.makeText(getApplicationContext(),
                                                         "The passwords entered do not match",
                                                         Toast.LENGTH_SHORT).show();
                                      }
                                      else if (selectedRoleIndex == null)
                                      {
                                          Toast.makeText(getApplicationContext(),
                                                         "You have to select a role to continue",
                                                         Toast.LENGTH_SHORT).show();
                                      }
                                      else
                                      {
                                          String selectedRole = accountManager.getAvailableRoles().get(
                                              selectedRoleIndex);
                                          UserAccount account;

                                          switch (selectedRole)
                                          {
                                              case "Employee":
                                                  account = new EmployeeAccount(username, password,
                                                                                firstName, lastName,
                                                                                email);
                                                  break;
                                              case "Customer":
                                                  account = new CustomerAccount(username, password,
                                                                                firstName, lastName,
                                                                                email);
                                                  break;
                                              default:
                                                  throw new IllegalArgumentException(
                                                      "Unknown role selected");
                                          }

                                          try
                                          {
                                              accountManager.createAccount(account);
                                          }
                                          catch (IllegalArgumentException e)
                                          {
                                              Toast.makeText(getApplicationContext(),
                                                             e.getMessage(),
                                                             Toast.LENGTH_LONG).show();
                                              return;
                                          }

                                          Toast.makeText(getApplicationContext(),
                                                         "Successfully registered. Logging in...",
                                                         Toast.LENGTH_LONG).show();

                                          Intent intent = new Intent(getApplicationContext(),
                                                                     WelcomeActivity.class);
                                          intent.putExtra("verifiedAccount", account);
                                          startActivity(intent);
                                      }
                                  });

    }

    private void setSelectedRole(Integer role)
    {
        selectedRoleIndex = role + 1;
    }
}
