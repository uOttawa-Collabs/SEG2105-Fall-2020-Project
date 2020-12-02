package team.returnteamname.servicenovigrad;

import static org.junit.Assert.*;
import org.junit.Test;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import team.returnteamname.servicenovigrad.account.Account;
import team.returnteamname.servicenovigrad.account.AdminAccount;
import team.returnteamname.servicenovigrad.account.CustomerAccount;
import team.returnteamname.servicenovigrad.account.EmployeeAccount;
import team.returnteamname.servicenovigrad.account.UserAccount;
import team.returnteamname.servicenovigrad.manager.interfaces.IManagerCallback;
import team.returnteamname.servicenovigrad.manager.AccountManager;

public class AccountManagerTest
{
    @Test
    public void checkAvailableRoles()
    {
        AccountManager accountManager = AccountManager.getInstance();
        String[] availableRoles = new String[3];
        availableRoles[0] = "Administrator";
        availableRoles[1] = "Employee";
        availableRoles[2] = "Customer";

        assertEquals("Check available roles from database", availableRoles, accountManager.getAvailableRoles());


    }


}
