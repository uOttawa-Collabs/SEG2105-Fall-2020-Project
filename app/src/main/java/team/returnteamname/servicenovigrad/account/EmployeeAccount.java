package team.returnteamname.servicenovigrad.account;

public class EmployeeAccount extends Account
{
    private String username, password, role, email;
    public EmployeeAccount(String name, String password, String email){
        username = name;
        this.password = password;
        this.email = email;
        role = "employee";
    }
}
