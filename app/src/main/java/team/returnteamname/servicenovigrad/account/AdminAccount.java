package team.returnteamname.servicenovigrad.account;

import team.returnteamname.servicenovigrad.manager.AccountManager;

public class AdminAccount extends Account
{
    private AccountManager accountManager;

    public AdminAccount(String username, String password)
    {
        this(username, password, "Administrator");
    }

    AdminAccount(String username, String password, String role)
    {
        super(username, password, role);
    }

    AdminAccount(String username, String password, String role,
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
