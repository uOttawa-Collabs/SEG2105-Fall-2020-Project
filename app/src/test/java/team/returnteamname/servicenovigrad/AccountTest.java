package team.returnteamname.servicenovigrad;

import com.google.firebase.firestore.auth.User;

import static org.junit.Assert.*;
import org.junit.Test;

import team.returnteamname.servicenovigrad.account.CustomerAccount;
import team.returnteamname.servicenovigrad.account.EmployeeAccount;


public class AccountTest
{

    @Test
    public void checkEmployeeFirstName(){
        EmployeeAccount aEmployeeUser = new EmployeeAccount("benEmployee", "123456", "Employee", "Ben", "Alex", "benalex@gmail.com" );
        assertEquals("Check the first name of the employee", "Ben", aEmployeeUser.getFirstName());
    }

    @Test
    public void checkEmployeeLastName(){
        EmployeeAccount aEmployeeUser = new EmployeeAccount("benEmployee", "123456", "Employee", "Ben", "Alex", "benalex@gmail.com" );
        assertEquals("Check the last name of the employee", "Alex", aEmployeeUser.getLastName());
    }

    @Test
    public void checkEmployeeUserName(){
        EmployeeAccount aEmployeeUser = new EmployeeAccount("benEmployee", "123456", "Employee", "Ben", "Alex", "benalex@gmail.com" );
        assertEquals("Check the username of the employee", "benEmployee", aEmployeeUser.getUsername());
    }

    @Test
    public void checkEmployeePassword(){
        EmployeeAccount aEmployeeUser = new EmployeeAccount("benEmployee", "123456", "Employee", "Ben", "Alex", "benalex@gmail.com" );
        assertEquals("Check the password of the employee", "123456", aEmployeeUser.getPassword());
    }

    @Test
    public void checkEmployeeRole(){
        EmployeeAccount aEmployeeUser = new EmployeeAccount("benEmployee", "123456", "Employee", "Ben", "Alex", "benalex@gmail.com" );
        assertEquals("Check the role of the employee", "Employee", aEmployeeUser.getRole());
    }

    @Test
    public void checkEmployeeEmail(){
        EmployeeAccount aEmployeeUser = new EmployeeAccount("benEmployee", "123456", "Employee", "Ben", "Alex", "benalex@gmail.com" );
        assertEquals("Check the email of the employee", "benalex@gmail.com", aEmployeeUser.getEmail());
    }

    @Test
    public void checkCustomerRole(){
        CustomerAccount aCustomerUser = new CustomerAccount("Jimmy", "112233","Perry", "Chad", "perrychad@gmail.com" );
        assertEquals("Check the role of the customer", "Customer", aCustomerUser.getRole());
    }

}
