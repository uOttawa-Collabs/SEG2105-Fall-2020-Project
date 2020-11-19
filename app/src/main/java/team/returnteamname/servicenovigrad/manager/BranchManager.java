package team.returnteamname.servicenovigrad.manager;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import team.returnteamname.servicenovigrad.account.Account;
import team.returnteamname.servicenovigrad.account.AdminAccount;
import team.returnteamname.servicenovigrad.account.EmployeeAccount;
import team.returnteamname.servicenovigrad.account.UserAccount;
import team.returnteamname.servicenovigrad.manager.interfaces.IManagerCallback;
import team.returnteamname.servicenovigrad.service.Service;
import team.returnteamname.servicenovigrad.service.document.Document;
import team.returnteamname.servicenovigrad.service.document.FormDocument;

public class BranchManager
{
    private static final BranchManager                 INSTANCE         = new BranchManager();
    private static final AccountManager                ACCOUNT_MANAGER  = AccountManager.getInstance();
    private final        FirebaseDatabase              firebaseDatabase = FirebaseDatabase.getInstance();
    private final        Map<String, IManagerCallback> managerCallbacks = new HashMap<>();

    private ArrayList<String>                                         branchServices;
    private ArrayList<String>                                         availableServices;
    private boolean                                                   initialized = false;
    private HashMap<String, HashMap<String, String>>                  namesAndServices;

    private BranchManager() {}

    public static BranchManager getInstance()
    {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public void initialize()
    {
        if (ACCOUNT_MANAGER.isInitialized())
        {
            DatabaseReference reference = firebaseDatabase.getReference();

            reference.child("employeeServices").addValueEventListener(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        namesAndServices = (HashMap<String, HashMap<String, String>>) snapshot.getValue();
                        checkIfInitialized();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        throw error.toException();
                    }
                });

            reference.child("availableServices").addValueEventListener(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        availableServices = (ArrayList<String>) snapshot.getValue();
                        checkIfInitialized();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        throw error.toException();
                    }
                });
        }
    }

    public boolean isInitialized()
    {
        return initialized;
    }

    public String[] getBranchServicesName(Account account)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(account) != null)
            {
                Map<String, String> branchServiceName = new HashMap<String, String>();
                branchServiceName = namesAndServices.get(account.getUsername());
                branchServices = new ArrayList<String>();
                for(String key: branchServiceName.keySet())
                {
                    String value = branchServiceName.get(key);
                    branchServices.add(value);
                }
                return branchServices == null ? null : branchServices.toArray(new String[0]);
            }
            else
                throw new IllegalArgumentException("Invalid account credential");
        }
        else
            throw new RuntimeException("Branch manager is not ready");
    }

    public String[] getAllServicesName(Account account)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(account) != null)

                return availableServices == null ? null : availableServices.toArray(new String[0]);
            else
                throw new IllegalArgumentException("Invalid account credential");
        }
        else
            throw new RuntimeException("Service manager is not ready");
    }

    public Service[] getAccountServices(UserAccount account, String branchName)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(account) != null)
            {
                throw new IllegalArgumentException("Not implemented yet"); // TODO
            }
            else
                throw new IllegalArgumentException("Invalid account credential");
        }
        else
            throw new RuntimeException("Branch manager is not ready");
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
        initialized = ACCOUNT_MANAGER.isInitialized();

        if (initialized)
        {
            for (Map.Entry<String, IManagerCallback> entry : managerCallbacks.entrySet())
            {
                entry.getValue().onInitialize();
            }
        }
    }
}