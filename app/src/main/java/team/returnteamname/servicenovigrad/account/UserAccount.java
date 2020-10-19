package team.returnteamname.servicenovigrad.account;

public abstract class UserAccount extends Account
{
    private String firstName;
    private String lastName;
    private String email;

    protected UserAccount(String username, String password, String role, String firstName,
                          String lastName, String email)
    {
        super(username, password, role);
        this.firstName = firstName;
        this.lastName  = lastName;
        this.email     = email;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }
}
