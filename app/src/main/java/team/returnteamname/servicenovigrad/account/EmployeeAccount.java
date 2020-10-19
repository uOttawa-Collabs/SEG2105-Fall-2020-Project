package team.returnteamname.servicenovigrad.account;

public class EmployeeAccount extends UserAccount
{
    private String firstName;
    private String lastName;
    private String email;

    public EmployeeAccount(String username, String password, String firstName,
                           String lastName, String email)
    {
        this(username, password, "Employee", firstName, lastName, email);
    }

    public EmployeeAccount(String username, String password, String role, String firstName,
                           String lastName, String email)
    {
        super(username, password, role, firstName, lastName, email);
    }
}
