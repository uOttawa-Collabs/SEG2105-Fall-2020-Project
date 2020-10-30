package team.returnteamname.servicenovigrad.manager;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import team.returnteamname.servicenovigrad.account.Account;
import team.returnteamname.servicenovigrad.account.AdminAccount;
import team.returnteamname.servicenovigrad.account.EmployeeAccount;
import team.returnteamname.servicenovigrad.account.UserAccount;
import team.returnteamname.servicenovigrad.manager.interfaces.IManagerCallback;
import team.returnteamname.servicenovigrad.service.Service;

public class ServiceManager
{
    private static final ServiceManager   INSTANCE         = new ServiceManager();
    private static final AccountManager   ACCOUNT_MANAGER  = AccountManager.getInstance();
    private final        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private              AdminAccount     adminAccount;

    private HashMap<String, Service> availableServices;

    private final Map<String, IManagerCallback> managerCallbacks = new HashMap<>();
    private       boolean                       initialized      = false;

    private ServiceManager() {}

    public static ServiceManager getInstance()
    {
        return INSTANCE;
    }

    @SuppressWarnings("unchecked")
    public void initialize()
    {
        if (ACCOUNT_MANAGER.isInitialized())
        {
            adminAccount = ACCOUNT_MANAGER.getAdminAccount();

            DatabaseReference reference = firebaseDatabase.getReference();
            reference.child("availableServices").addValueEventListener(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        availableServices = (HashMap<String, Service>) snapshot.getValue();
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

    public HashMap<String, Service> getAllServices(Account account)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(account))
            {
                if (account instanceof AdminAccount || account instanceof EmployeeAccount)
                {
                    return availableServices;
                }
                else
                    throw new IllegalArgumentException(
                        "Only administrator and employees can query all services");
            }
            else
                throw new IllegalArgumentException("Invalid account credential");
        }
        else
            throw new RuntimeException("Service manager is not ready");
    }

    public List<Service> getAccountServices(UserAccount account, String branchName)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(account))
            {
                throw new IllegalArgumentException("Not implemented yet"); // TODO
            }
            else
                throw new IllegalArgumentException("Invalid account credential");
        }
        else
            throw new RuntimeException("Service manager is not ready");
    }

    public void addService(AdminAccount adminAccount, Service service)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(adminAccount))
            {
                firebaseDatabase.getReference().child("availableServices").child(
                    service.getName()).setValue(service);
            }
            else
                throw new IllegalArgumentException("Invalid administrator credential");
        }
        else
            throw new RuntimeException("Service manager is not ready");
    }

    public void deleteService(AdminAccount adminAccount, String name)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(adminAccount))
            {
                firebaseDatabase.getReference().child("availableServices").child(
                    name).removeValue();

                // TODO: May affect branches that was assigned to the deleted service. Need to modify this method during phase 3.
            }
            else
                throw new IllegalArgumentException("Invalid administrator credential");
        }
        else
            throw new RuntimeException("Service manager is not ready");
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
        initialized = availableServices != null;

        if (initialized)
        {
            for (Map.Entry<String, IManagerCallback> entry : managerCallbacks.entrySet())
            {
                entry.getValue().onInitialize();
            }
        }
    }
}
