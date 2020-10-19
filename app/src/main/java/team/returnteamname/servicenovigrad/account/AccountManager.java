package team.returnteamname.servicenovigrad.account;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class AccountManager
{
    private static final AccountManager   INSTANCE         = new AccountManager();
    private final        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private              AdminAccount     adminAccount;

    private ArrayList<String> accounts;
    private ArrayList<String> availableRoles;
    private ArrayList<Long>   roleMembers;
    private ArrayList<Long>   roles;
    private ArrayList<String> shadow;

    private Map<String, AccountManagerCallback> accountManagerCallbacks;
    private boolean                             isInitialized = false;

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

        firebaseDatabase.getReference().child("roleMembers").addValueEventListener(
            new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    roleMembers = (ArrayList<Long>) snapshot.getValue();
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
                    roles = (ArrayList<Long>) snapshot.getValue();
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
                    shadow = (ArrayList<String>) snapshot.getValue();
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


            }
            else if (account instanceof CustomerAccount)
            {
                CustomerAccount customerAccount = (CustomerAccount) account;
                // TODO: Create the account in the database
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
            int uid = accounts.indexOf(account.getUsername());

            if (uid < 0)
                return false;

            try
            {
                String password = shadow.get(uid);
                return password.equals(account.getPassword());
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

    private void checkIfInitialized()
    {
        isInitialized = accounts != null && availableRoles != null && roleMembers != null && roles != null && shadow != null;

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
        if (roles.get(0) > Integer.MAX_VALUE)
            throw new IndexOutOfBoundsException("role index is too large");

        adminAccount = new AdminAccount(accounts.get(0), shadow.get(0), availableRoles.get(
            Integer.parseInt(Long.toString(roles.get(0)))));

        adminAccount.setAccountManager(this);
    }

    public interface AccountManagerCallback
    {
        void onInitialize();
    }
}
