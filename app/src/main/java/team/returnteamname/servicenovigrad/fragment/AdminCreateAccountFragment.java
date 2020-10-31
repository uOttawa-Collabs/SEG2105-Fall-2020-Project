package team.returnteamname.servicenovigrad.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.Arrays;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.AdminAccount;
import team.returnteamname.servicenovigrad.account.CustomerAccount;
import team.returnteamname.servicenovigrad.account.EmployeeAccount;
import team.returnteamname.servicenovigrad.account.UserAccount;
import team.returnteamname.servicenovigrad.manager.AccountManager;

public class AdminCreateAccountFragment extends Fragment
{
    Integer selectedRoleIndex;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View   view   = inflater.inflate(R.layout.fragment_admin_create_account, container, false);
        Bundle bundle = getArguments();

        if (bundle != null)
        {
            AdminAccount account = (AdminAccount) bundle.getSerializable("account");

            EditText[] editTextValues = new EditText[6];

            editTextValues[0] = view.findViewById(R.id.editTextFirstName);
            editTextValues[1] = view.findViewById(R.id.editTextLastName);
            editTextValues[2] = view.findViewById(R.id.editTextEmail);
            editTextValues[3] = view.findViewById(R.id.editTextUsername);
            editTextValues[4] = view.findViewById(R.id.editTextPassword);
            editTextValues[5] = view.findViewById(R.id.editTextPasswordConfirm);

            AccountManager accountManager = AccountManager.getInstance();

            Button button = view.findViewById(R.id.buttonRegisterSubmit);

            String[] availableRoles = accountManager.getAvailableRoles();

            Spinner spinner = view.findViewById(R.id.spinnerRoleSelector);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                                                              R.layout.activity_register_spinner,
                                                              Arrays.copyOfRange(availableRoles, 1,
                                                                                 availableRoles.length));


            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
            {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
                {
                    selectedRoleIndex = position + 1;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent)
                {
                    selectedRoleIndex = null;
                }
            });

            button.setOnClickListener(
                v ->
                {
                    String firstName       = editTextValues[0].getText().toString().trim();
                    String lastName        = editTextValues[1].getText().toString().trim();
                    String email           = editTextValues[2].getText().toString().trim();
                    String username        = editTextValues[3].getText().toString().trim();
                    String password        = editTextValues[4].getText().toString().trim();
                    String passwordConfirm = editTextValues[5].getText().toString().trim();

                    if (!accountManager.isInitialized())
                    {
                        Toast.makeText(getContext(),
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
                        Toast.makeText(getContext(),
                                       "All fields are required",
                                       Toast.LENGTH_SHORT).show();
                    }
                    else if (!email.matches("^[\\w-.]+@[\\w-]+\\.+[\\w-]{2,}$"))
                    {
                        Toast.makeText(getContext(),
                                       "Invalid email address",
                                       Toast.LENGTH_SHORT).show();
                    }
                    else if (!password.equals(passwordConfirm))
                    {
                        Toast.makeText(getContext(),
                                       "The passwords entered do not match",
                                       Toast.LENGTH_SHORT).show();
                    }
                    else if (selectedRoleIndex == null)
                    {
                        Toast.makeText(getContext(),
                                       "You have to select a role to continue",
                                       Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String      selectedRole = accountManager.getAvailableRoles()[selectedRoleIndex];
                        UserAccount userAccount;

                        switch (selectedRole)
                        {
                            case "Employee":
                                userAccount = new EmployeeAccount(username, password,
                                                                  firstName, lastName,
                                                                  email);
                                break;
                            case "Customer":
                                userAccount = new CustomerAccount(username, password,
                                                                  firstName, lastName,
                                                                  email);
                                break;
                            default:
                                throw new IllegalArgumentException(
                                    "Unknown role selected");
                        }

                        try
                        {
                            accountManager.createAccount(userAccount);
                        }
                        catch (IllegalArgumentException e)
                        {
                            Toast.makeText(getContext(),
                                           e.getMessage(),
                                           Toast.LENGTH_LONG).show();
                            return;
                        }

                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                    }
                });
        }
        else
            throw new IllegalArgumentException("Invalid argument");

        return view;
    }
}
