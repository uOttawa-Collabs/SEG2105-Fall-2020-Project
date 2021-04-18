package team.returnteamname.servicenovigrad.account;

public class CustomerAccount extends UserAccount
{
    private String firstName;
    private String lastName;
    private String email;

    public CustomerAccount(String username, String password, String firstName,
                           String lastName, String email)
    {
        this(username, password, "Customer", firstName, lastName, email);
    }

    public CustomerAccount(String username, String password, String role, String firstName,
                           String lastName, String email)
    {
        super(username, password, role, firstName, lastName, email);
    }
}
