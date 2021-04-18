package team.returnteamname.servicenovigrad.account;

import java.io.Serializable;

public class Account implements Serializable
{
    private final String username;
    private final String password;
    private final String role;

    public Account(String username, String password)
    {
        this(username, password, "undefined");
    }

    public Account(String username, String password, String role)
    {
        this.username = username;
        this.password = password;
        this.role     = role;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public String getRole()
    {
        return role;
    }
}
