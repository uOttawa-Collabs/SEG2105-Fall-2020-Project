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

    @Test
    public void checkEmployeeLastName(){
        EmployeeAccount aUser = new EmployeeAccount("benEmployee", "123456", "Employee", "Ben", "Alex", "benalex@gmail.com" );
        assertEquals("Check the last name of the employee", "Alex", aUser.getLastName());
    }

    @Test
    public void checkEmployeeUserName(){
        EmployeeAccount aUser = new EmployeeAccount("benEmployee", "123456", "Employee", "Ben", "Alex", "benalex@gmail.com" );
        assertEquals("Check the username of the employee", "benEmployee", aUser.getUsername());
    }

    @Test
    public void checkPassword(){
        EmployeeAccount aUser = new EmployeeAccount("benEmployee", "123456", "Employee", "Ben", "Alex", "benalex@gmail.com" );
        assertEquals("Check the password of the employee", "123456", aUser.getPassword());
    }

    @Test
    public void checkRole(){
        EmployeeAccount aUser = new EmployeeAccount("benEmployee", "123456", "Employee", "Ben", "Alex", "benalex@gmail.com" );
        assertEquals("Check the role of the employee", "Employee", aUser.getRole());
    }

    @Test
    public void checkEmail(){
        EmployeeAccount aUser = new EmployeeAccount("benEmployee", "123456", "Employee", "Ben", "Alex", "benalex@gmail.com" );
        assertEquals("Check the email of the employee", "benalex@gmail.com", aUser.getEmail());
    }

}
