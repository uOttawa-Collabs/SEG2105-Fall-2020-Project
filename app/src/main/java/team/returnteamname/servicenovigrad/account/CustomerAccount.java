package team.returnteamname.servicenovigrad.account;

public class CustomerAccount extends Account
{
    private String username, password, role, email;
    public CustomerAccount(String name, String password, String email){
        username = name;
        this.password = password;
        this.email = email;
        role = "customer";
    }
}
