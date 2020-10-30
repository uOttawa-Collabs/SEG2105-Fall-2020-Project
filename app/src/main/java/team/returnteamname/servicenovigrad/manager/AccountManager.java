package team.returnteamname.servicenovigrad.manager;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import team.returnteamname.servicenovigrad.account.Account;
import team.returnteamname.servicenovigrad.account.AdminAccount;
import team.returnteamname.servicenovigrad.account.UserAccount;
import team.returnteamname.servicenovigrad.manager.interfaces.IManagerCallback;

public class AccountManager
{
    private static final AccountManager   INSTANCE         = new AccountManager();
    private final        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private              AdminAccount     adminAccount;

    private ArrayList<String>                        accounts;
    private ArrayList<String>                        availableRoles;
    private HashMap<String, String>                  emails;
    private HashMap<String, HashMap<String, String>> names;
    private HashMap<String, String>                  roleMembers;
    private HashMap<String, String>                  roles;
    private HashMap<String, String>                  shadow;

    private final Map<String, IManagerCallback> managerCallbacks = new HashMap<>();
    private       boolean                       initialized      = false;

    // Singleton
    private AccountManager() {}

    public static AccountManager getInstance()
    {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public void initialize()
    {
        DatabaseReference reference = firebaseDatabase.getReference();

        reference.child("accounts").addValueEventListener(
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

        reference.child("availableRoles").addValueEventListener(
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

        reference.child("emails").addValueEventListener(
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

        reference.child("names").addValueEventListener(
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

        reference.child("roleMembers").addValueEventListener(
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

        reference.child("roles").addValueEventListener(
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

        reference.child("shadow").addValueEventListener(
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

        addManagerCallback("bindAdminAccount", this::bindAdminAccount);
    }

    AdminAccount getAdminAccount()
    {
        return adminAccount;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<String> getAvailableRoles()
    {
        return (ArrayList<String>) availableRoles.clone();
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

    public void createAccount(UserAccount account)
    {
        if (initialized)
        {
            if (accounts.contains(account.getUsername()))
                throw new IllegalArgumentException("Username exists");

            // Create a new account
            int               uid               = accounts.size();
            DatabaseReference databaseReference = firebaseDatabase.getReference();

            databaseReference.child("accounts").child(Integer.toString(uid)).setValue(
                account.getUsername());
            databaseReference.child("shadow").child(account.getUsername()).setValue(
                account.getPassword());
            databaseReference.child("emails").child(account.getUsername()).setValue(
                account.getEmail());
            databaseReference.child("names").child(account.getUsername()).child(
                "firstName").setValue(account.getFirstName());
            databaseReference.child("names").child(account.getUsername()).child(
                "lastName").setValue(account.getLastName());
            databaseReference.child("roleMembers").child(account.getRole()).child(
                account.getUsername()).setValue(true);
            databaseReference.child("roles").child(account.getUsername()).setValue(
                account.getRole());
        }
        else
            throw new RuntimeException("Account manager is not ready");

    }

    public boolean verifyAccount(Account account)
    {
        if (initialized)
        {
            if (!accounts.contains(account.getUsername()))
                return false;

            try
            {
                String password = shadow.get(account.getUsername());
                if (password != null && password.equals(account.getPassword()))
                {
                    account.setRole(roles.get(account.getUsername()));
                    if (!account.getRole().equals("Administrator"))
                    {
                        UserAccount userAccount = (UserAccount) account;
                        userAccount.setFirstName(names.get(account.getUsername()).get("firstName"));
                        userAccount.setLastName(names.get(account.getUsername()).get("lastName"));
                        userAccount.setEmail(emails.get(account.getUsername()));
                    }
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

    public void deleteAccount(String username)
    {
        if (initialized)
        {
            if (username.equals(adminAccount.getUsername()))
                throw new IllegalArgumentException("Reject deleting admin account");

            int index = accounts.indexOf(username);

            if (index == -1)
                throw new IllegalArgumentException("Username does not exist");
            else
            {
                DatabaseReference reference = firebaseDatabase.getReference();

                reference.child("accounts").child(Integer.toString(index)).removeValue();
                reference.child("emails").child(username).removeValue();
                reference.child("names").child(username).removeValue();
                reference.child("roleMembers").child(Objects.requireNonNull(roles.get(username))).child(username).removeValue();
                reference.child("roles").child(username).removeValue();
                reference.child("shadow").child(username).removeValue();
            }
        }
        else
            throw new RuntimeException("Account manager is not ready");
    }

    public boolean isInitialized()
    {
        return initialized;
    }

    public void removeManagerCallback(String identifier)
    {
        managerCallbacks.remove(identifier);
    }

    public void addManagerCallback(String identifier, IManagerCallback callback)
    {
        managerCallbacks.put(identifier, callback);
    }

    synchronized private void checkIfInitialized()
    {
        initialized = accounts != null
                      && availableRoles != null
                      && emails != null
                      && names != null
                      && roleMembers != null
                      && roles != null
                      && shadow != null;

        if (initialized)
        {
            for (Map.Entry<String, IManagerCallback> entry : managerCallbacks.entrySet())
            {
                entry.getValue().onInitialize();
            }
        }
    }

    private void bindAdminAccount()
    {
        adminAccount = new AdminAccount(accounts.get(0), shadow.get(accounts.get(0)));
        adminAccount.setAccountManager(this);
    }
}
