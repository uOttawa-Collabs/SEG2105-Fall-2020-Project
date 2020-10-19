package team.returnteamname.servicenovigrad.account;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccountManager
{
    private static final String           DEFAULT_ADMIN_ACCOUNT_NAME = "admin";
    private static final AccountManager   INSTANCE                   = new AccountManager();
    private final        FirebaseDatabase firebaseDatabase           = FirebaseDatabase.getInstance();
    private              AdminAccount     adminAccount;

    private ArrayList<String>                        accounts;
    private ArrayList<String>                        availableRoles;
    private HashMap<String, String>                  emails;
    private HashMap<String, HashMap<String, String>> names;
    private HashMap<String, String>                  roleMembers;
    private HashMap<String, String>                  roles;
    private HashMap<String, String>                  shadow;

    private final Map<String, AccountManagerCallback> accountManagerCallbacks = new HashMap<>();
    private       boolean                             isInitialized           = false;

    // Singleton
    private AccountManager() {}

    public static AccountManager getInstance()
    {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public void initialize()
    {
        firebaseDatabase.getReference().child("accounts").addValueEventListener(
            new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    accounts = (ArrayList<String>) snapshot.getValue();
                    checkIfInitialized();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {
                    throw error.toException();
                }
            });

        firebaseDatabase.getReference().child("availableRoles").addValueEventListener(
            new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    availableRoles = (ArrayList<String>) snapshot.getValue();
                    checkIfInitialized();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {
                    throw error.toException();
                }
            });

        firebaseDatabase.getReference().child("emails").addValueEventListener(
            new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    emails = (HashMap<String, String>) snapshot.getValue();
                    checkIfInitialized();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {
                    throw error.toException();
                }
            });

        firebaseDatabase.getReference().child("names").addValueEventListener(
            new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    names = (HashMap<String, HashMap<String, String>>) snapshot.getValue();
                    checkIfInitialized();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {
                    throw error.toException();
                }
            });

        firebaseDatabase.getReference().child("roleMembers").addValueEventListener(
            new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    roleMembers = (HashMap<String, String>) snapshot.getValue();
                    checkIfInitialized();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {
                    throw error.toException();
                }
            });

        firebaseDatabase.getReference().child("roles").addValueEventListener(
            new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    roles = (HashMap<String, String>) snapshot.getValue();
                    checkIfInitialized();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {
                    throw error.toException();
                }
            });

        firebaseDatabase.getReference().child("shadow").addValueEventListener(
            new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    shadow = (HashMap<String, String>) snapshot.getValue();
                    checkIfInitialized();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error)
                {
                    throw error.toException();
                }
            });

        addAccountManagerCallback("bindAdminAccount", this::bindAdminAccount);
    }

    public AdminAccount getAdminAccount()
    {
        return adminAccount;
    }

    public String[] getAvailableRoles()
    {
        return (String[]) availableRoles.toArray();
    }

    public String getAccountEmail(UserAccount account)
    {
        if (account != null)
            return emails.get(account.getUsername());
        else
            return null;
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> getAccountName(UserAccount account)
    {
        if (account != null)
            return (Map<String, String>) names.get(account.getUsername()).clone();
        else
            return null;
    }

    public String getAccountRole(Account account)
    {
        if (account != null)
            return roles.get(account.getUsername());
        else
            return null;
    }

    public void createAccount(Account account)
    {
        if (isInitialized)
        {
            if (account instanceof AdminAccount)
            {
                // Reject creation
                throw new IllegalArgumentException("Cannot create an admin account");
            }
            else if (account instanceof EmployeeAccount)
            {
                EmployeeAccount employeeAccount = (EmployeeAccount) account;

                if (accounts.contains(employeeAccount.getUsername()))
                    throw new IllegalArgumentException("Username exists");

                // Create a new account
                int               uid               = accounts.size();
                DatabaseReference databaseReference = firebaseDatabase.getReference();

                databaseReference.child("account").child(Integer.toString(uid)).setValue(
                    employeeAccount.getUsername());
                databaseReference.child("shadow").child(account.getUsername()).setValue(
                    employeeAccount.getPassword());
                databaseReference.child("names").child(account.getUsername()).child(
                    "firstName").setValue(employeeAccount.getFirstName());
                databaseReference.child("names").child(account.getUsername()).child(
                    "lastName").setValue(employeeAccount.getLastName());
                databaseReference.child("roleMembers").child(account.getUsername()).child(
                    "Employer").child(Integer.toString(roleMembers.size())).setValue(
                    account.getUsername());
                databaseReference.child("roles").child(account.getUsername()).setValue("Employer");
            }
            else if (account instanceof CustomerAccount)
            {
                CustomerAccount customerAccount = (CustomerAccount) account;
                if (accounts.contains(customerAccount.getUsername()))
                    throw new IllegalArgumentException("Username exists");

                // Create a new account
                int               uid               = accounts.size();
                DatabaseReference databaseReference = firebaseDatabase.getReference();

                databaseReference.child("account").child(Integer.toString(uid)).setValue(
                    customerAccount.getUsername());
                databaseReference.child("shadow").child(account.getUsername()).setValue(
                    customerAccount.getPassword());
                databaseReference.child("names").child(account.getUsername()).child(
                    "firstName").setValue(customerAccount.getFirstName());
                databaseReference.child("names").child(account.getUsername()).child(
                    "lastName").setValue(customerAccount.getLastName());
                databaseReference.child("roleMembers").child(account.getUsername()).child(
                    "Customer").child(Integer.toString(roleMembers.size())).setValue(
                    account.getUsername());
                databaseReference.child("roles").child(account.getUsername()).setValue("Customer");
            }
            else
                throw new IllegalArgumentException("Unknown account type");
        }
        else
            throw new RuntimeException("Account manager is not ready");

    }

    public boolean verifyAccount(Account account)
    {
        if (isInitialized)
        {
            if (!accounts.contains(account.getUsername()))
                return false;

            try
            {
                String password = shadow.get(account.getUsername());
                if (password != null && password.equals(account.getPassword()))
                {
                    account.setRole(roles.get(account.getUsername()));
                    return true;
                }
                else
                    return false;
            }
            catch (IndexOutOfBoundsException ignored)
            {
                return false;
            }
        }
        else
            throw new RuntimeException("Account manager is not ready");

    }

    public boolean isInitialized()
    {
        return isInitialized;
    }

    public void removeAccountManagerCallback(String identifier)
    {
        accountManagerCallbacks.remove(identifier);
    }

    public void addAccountManagerCallback(
        String identifier, AccountManagerCallback accountManagerCallback)
    {
        accountManagerCallbacks.put(identifier, accountManagerCallback);
    }

    synchronized private void checkIfInitialized()
    {
        isInitialized = accounts != null
                        && availableRoles != null
                        && emails != null
                        && names != null
                        && roleMembers != null
                        && roles != null
                        && shadow != null;

        if (isInitialized)
        {
            for (Map.Entry<String, AccountManagerCallback> entry : accountManagerCallbacks.entrySet())
            {
                entry.getValue().onInitialize();
            }
        }
    }

    private void bindAdminAccount()
    {
        adminAccount = new AdminAccount(accounts.get(0), shadow.get(DEFAULT_ADMIN_ACCOUNT_NAME),
                                        roles.get(DEFAULT_ADMIN_ACCOUNT_NAME));
        adminAccount.setAccountManager(this);
    }

    public interface AccountManagerCallback
    {
        void onInitialize();
    }
}
