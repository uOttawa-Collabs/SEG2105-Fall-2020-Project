package team.returnteamname.servicenovigrad;

import com.google.firebase.firestore.auth.User;

import static org.junit.Assert.*;
import org.junit.Test;

import team.returnteamname.servicenovigrad.account.Account;
import team.returnteamname.servicenovigrad.account.UserAccount;
import team.returnteamname.servicenovigrad.account.EmployeeAccount;

public class EmployeeAccountTest
{

    @Test
    public void checkEmployeeFirstName(){
        EmployeeAccount aUser = new EmployeeAccount("benEmployee", "123456", "Employee", "Ben", "Alex", "benalex@gmail.com" );
        assertEquals("Check the first name of the employee", "Ben", aUser.getFirstName());
    }

}
