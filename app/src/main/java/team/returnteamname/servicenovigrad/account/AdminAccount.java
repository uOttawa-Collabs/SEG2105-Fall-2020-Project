package team.returnteamname.servicenovigrad.account;

public class AdminAccount extends Account
{

    private String username, password, role;
    public AdminAccount(){
        username = "admin";
        this.password = "123admin456";
        role = "admin";
    }

}
