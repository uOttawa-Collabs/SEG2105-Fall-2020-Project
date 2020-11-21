package team.returnteamname.servicenovigrad.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.navigation.NavigationView;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.Account;
import team.returnteamname.servicenovigrad.account.UserAccount;
import team.returnteamname.servicenovigrad.fragment.AdminCreateAccountFragment;
import team.returnteamname.servicenovigrad.fragment.AdminCreateServiceFragment;
import team.returnteamname.servicenovigrad.fragment.AdminDeleteAccountFragment;
import team.returnteamname.servicenovigrad.fragment.AdminDeleteServiceFragment;
import team.returnteamname.servicenovigrad.fragment.EmployeeAddServiceFragment;
import team.returnteamname.servicenovigrad.fragment.EmployeeDeleteServiceFragment;
import team.returnteamname.servicenovigrad.fragment.EmployeeEditWorkingHours;
import team.returnteamname.servicenovigrad.fragment.EmployeeProcessServiceRequest;
import team.returnteamname.servicenovigrad.fragment.HomeFragment;

public class DashboardActivity extends AppCompatActivity
{
    private static final int ADMIN_CREATE_ACCOUNT = 0;
    private static final int ADMIN_DELETE_ACCOUNT = 1;
    private static final int ADMIN_CREATE_SERVICE = 2;
    private static final int ADMIN_DELETE_SERVICE = 3;

    private static final int EMPLOYEE_CREATE_SERVICE = 4;
    private static final int EMPLOYEE_DELETE_SERVICE = 5;
    private static final int EMPLOYEE_EDIT_WORKING_HOURS = 6;
    private static final int EMPLOYEE_PROCESS_SERVICE = 7;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        DrawerLayout   drawer         = findViewById(R.id.layoutDashboardActivity);
        NavigationView navigationView = findViewById(R.id.drawerNavigationView);

        Button buttonOpenDrawer = findViewById(R.id.buttonOpenDrawer);

        // Menu button event binding
        buttonOpenDrawer.setOnClickListener(v -> drawer.open());

        // Handle intent parameters
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if (bundle != null)
        {
            Account account = (Account) bundle.get("account");

            showLoginToast(account);
            setupDrawerContent(navigationView, account, drawer);

            switchFragment(account, HomeFragment.class);
        }
        else
            throw new IllegalArgumentException("Invalid argument");
    }

    private void setupDrawerContent(NavigationView navigationView, Account account,
                                    DrawerLayout drawer)
    {
        Menu menu = navigationView.getMenu();

        switch (account.getRole())
        {
            case "Administrator":
                menu.add(Menu.NONE, ADMIN_CREATE_ACCOUNT, Menu.NONE, "Create an Account");
                menu.add(Menu.NONE, ADMIN_DELETE_ACCOUNT, Menu.NONE, "Delete an Account");
                menu.add(Menu.NONE, ADMIN_CREATE_SERVICE, Menu.NONE, "Create a Service");
                menu.add(Menu.NONE, ADMIN_DELETE_SERVICE, Menu.NONE, "Delete a Service");
                break;
            case "Employee":
                menu.add(Menu.NONE, EMPLOYEE_CREATE_SERVICE, Menu.NONE, "Add a service");
                menu.add(Menu.NONE, EMPLOYEE_DELETE_SERVICE, Menu.NONE, "Delete a service");
                menu.add(Menu.NONE, EMPLOYEE_EDIT_WORKING_HOURS, Menu.NONE, "Edit working hours");
                menu.add(Menu.NONE, EMPLOYEE_PROCESS_SERVICE, Menu.NONE, "View service requests");

                break;
            case "Customer":
                break;
        }

        navigationView.setNavigationItemSelectedListener(
            item ->
            {
                drawerItemSelectionCallback(item, account, drawer);
                return true;
            });
    }

    private void drawerItemSelectionCallback(MenuItem menuItem, Account account,
                                             DrawerLayout drawer)
    {
        Class<? extends Fragment> fragmentClass = null;

        switch (account.getRole())
        {
            case "Administrator":
            {
                switch (menuItem.getItemId())
                {
                    case ADMIN_CREATE_ACCOUNT:
                        fragmentClass = AdminCreateAccountFragment.class;
                        break;
                    case ADMIN_DELETE_ACCOUNT:
                        fragmentClass = AdminDeleteAccountFragment.class;
                        break;
                    case ADMIN_CREATE_SERVICE:
                        fragmentClass = AdminCreateServiceFragment.class;
                        break;
                    case ADMIN_DELETE_SERVICE:
                        fragmentClass = AdminDeleteServiceFragment.class;
                        break;
                    default:
                        fragmentClass = HomeFragment.class;
                        break;
                }
                break;
            }
            case "Employee":
                switch (menuItem.getItemId()){
                    case EMPLOYEE_CREATE_SERVICE:
                        fragmentClass = EmployeeAddServiceFragment.class;
                        break;
                    case EMPLOYEE_DELETE_SERVICE:
                        fragmentClass = EmployeeDeleteServiceFragment.class;
                        break;
                    case EMPLOYEE_EDIT_WORKING_HOURS:
                        fragmentClass = EmployeeEditWorkingHours.class;
                        break;
                    case EMPLOYEE_PROCESS_SERVICE:
                        fragmentClass = EmployeeProcessServiceRequest.class;
                    default:
                        fragmentClass = HomeFragment.class;
                        break;
                }
                break;
            case "Customer":
                break;
        }

        switchFragment(account, fragmentClass);
        setTitleTextView(menuItem.getTitle().toString());
        drawer.close();
    }

    private void setTitleTextView(String title)
    {
        TextView textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setText(title);
    }

    private void showLoginToast(Account account)
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Welcome");
        if (!account.getRole().equals("Administrator"))
        {
            stringBuilder.append(' ').append(((UserAccount) account).getFirstName());
        }
        stringBuilder.append("! You are logged in as ").append(account.getRole()).append('.');
        Toast.makeText(getApplicationContext(), stringBuilder.toString(),
                       Toast.LENGTH_LONG).show();
    }

    private void switchFragment(Account account, Class<? extends Fragment> fragmentClass)
    {
        if (fragmentClass != null)
        {
            try
            {
                Fragment fragment = fragmentClass.newInstance();

                Bundle bundle = new Bundle();
                bundle.putSerializable("account", account);
                fragment.setArguments(bundle);

                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.layoutFragment, fragment).commit();

            }
            catch (Exception e)
            {
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
}
