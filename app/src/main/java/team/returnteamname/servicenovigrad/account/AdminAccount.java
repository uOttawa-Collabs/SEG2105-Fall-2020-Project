package team.returnteamname.servicenovigrad.account;

public class AdminAccount extends Account
{
    private AccountManager accountManager;

    public AdminAccount(String username, String password, String role)
    {
        super(username, password, role);
    }

    public AdminAccount(String username, String password, String role,
                        AccountManager accountManager)
    {
        super(username, password, role);
        this.accountManager = accountManager;
    }

    public AccountManager getAccountManager()
    {
        return accountManager;
    }

    public void setAccountManager(AccountManager accountManager)
    {
        this.accountManager = accountManager;
    }
}
