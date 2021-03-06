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

import team.returnteamname.servicenovigrad.account.Account;
import team.returnteamname.servicenovigrad.account.AdminAccount;
import team.returnteamname.servicenovigrad.manager.interfaces.IManagerCallback;
import team.returnteamname.servicenovigrad.service.Service;
import team.returnteamname.servicenovigrad.service.document.Document;
import team.returnteamname.servicenovigrad.service.document.FormDocument;

public class ServiceManager
{
    private static final ServiceManager                INSTANCE         = new ServiceManager();
    private static final AccountManager                ACCOUNT_MANAGER  = AccountManager.getInstance();
    private final        FirebaseDatabase              firebaseDatabase = FirebaseDatabase.getInstance();
    private final        Map<String, IManagerCallback> managerCallbacks = new HashMap<>();
    private              AdminAccount                  adminAccount;

    private ArrayList<String>                                         availableServices;
    private HashMap<String, HashMap<String, String>>                  serviceDocuments;
    private HashMap<String, HashMap<String, HashMap<String, String>>> serviceDocumentsFormExtra;
    private boolean                                                   initialized = false;


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
                        availableServices = (ArrayList<String>) snapshot.getValue();
                        checkIfInitialized();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        throw error.toException();
                    }
                });

            reference.child("serviceDocuments").addValueEventListener(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        serviceDocuments = (HashMap<String, HashMap<String, String>>) snapshot.getValue();
                        checkIfInitialized();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        throw error.toException();
                    }
                });

            reference.child("serviceDocumentsFormExtra").addValueEventListener(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        serviceDocumentsFormExtra = (HashMap<String, HashMap<String, HashMap<String, String>>>) snapshot.getValue();
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

    public HashMap<String, HashMap<String, String>> getAllServiceDocuments(
        AdminAccount account)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(account) != null)
                return serviceDocuments;
            else
                throw new IllegalArgumentException("Invalid account credential");
        }
        else
            throw new RuntimeException("Service manager is not ready");
    }

    public HashMap<String, HashMap<String, HashMap<String, String>>> getAllServiceDocumentsFormExtra(
        AdminAccount account)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(account) != null)
                return serviceDocumentsFormExtra;
            else
                throw new IllegalArgumentException("Invalid account credential");
        }
        else
            throw new RuntimeException("Service manager is not ready");
    }

    public void createService(@NotNull AdminAccount adminAccount, @NotNull Service service)
    {
        if (initialized)
        {
            DatabaseReference databaseReference = firebaseDatabase.getReference();

            if (ACCOUNT_MANAGER.verifyAccount(adminAccount) != null)
            {
                String                serviceName = service.getName();
                Map<String, Document> documentMap = service.getDocumentMap();

                if (availableServices != null && availableServices.contains(serviceName))
                    throw new IllegalArgumentException("Service name exists");

                int sid = availableServices == null ? 0 : availableServices.size();
                databaseReference.child("availableServices").child(Integer.toString(sid)).setValue(
                    serviceName);

                for (String key : documentMap.keySet())
                {
                    Document document = documentMap.get(key);

                    if (document != null)
                    {
                        String documentName = document.getName();
                        String documentType = document.getType();

                        databaseReference.child("serviceDocuments").child(serviceName).child(
                            documentName).setValue(documentType);

                        if (document instanceof FormDocument)
                        {
                            FormDocument        formDocument = (FormDocument) document;
                            Map<String, String> formMap      = formDocument.getFormMap();

                            databaseReference.child("serviceDocumentsFormExtra").child(
                                serviceName).child(documentName).setValue(formMap);
                        }
                    }
                }
            }
            else
                throw new IllegalArgumentException("Invalid administrator credential");
        }
        else
            throw new RuntimeException("Service manager is not ready");
    }

    public void updateService(@NotNull AdminAccount account, @NotNull String oldServiceName,
                              @NotNull Service service)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(account) != null)
            {
                int sid = availableServices.indexOf(oldServiceName);
                if (sid == -1)
                    throw new IllegalArgumentException("Old service name does not exist");

                DatabaseReference     databaseReference = firebaseDatabase.getReference();
                Map<String, Document> documentMap       = service.getDocumentMap();

                databaseReference.child("availableServices").child(Integer.toString(sid)).setValue(
                    service.getName());

                HashMap<String, String> employeeMap = BranchManager.getInstance().getAllBranchByService(
                    account).get(oldServiceName);
                databaseReference.child("branchServices").child(oldServiceName).removeValue();
                databaseReference.child("branchServices").child(service.getName()).setValue(
                    employeeMap);

                HashMap<String, HashMap<String, String>> employeeServiceMap = BranchManager
                    .getInstance()
                    .getAllEmployeeServices(account);

                for (String employee : employeeServiceMap.keySet())
                {
                    HashMap<String, String> serviceMap = employeeServiceMap.get(employee);
                    if (serviceMap.containsKey(oldServiceName))
                    {
                        databaseReference.child("employeeServices").child(employee).child(
                            oldServiceName).removeValue();
                        databaseReference.child("employeeServices").child(employee).child(
                            service.getName()).setValue(service.getName());
                    }
                }

                databaseReference.child("serviceDocuments").child(oldServiceName).removeValue();
                databaseReference.child("serviceDocumentsFormExtra").child(
                    oldServiceName).removeValue();

                for (String key : documentMap.keySet())
                {
                    Document document = documentMap.get(key);

                    if (document != null)
                    {
                        String documentName = document.getName();
                        String documentType = document.getType();

                        databaseReference.child("serviceDocuments").child(service.getName()).child(
                            documentName).setValue(documentType);

                        if (document instanceof FormDocument)
                        {
                            FormDocument        formDocument = (FormDocument) document;
                            Map<String, String> formMap      = formDocument.getFormMap();

                            databaseReference.child("serviceDocumentsFormExtra").child(
                                service.getName()).child(documentName).setValue(formMap);
                        }
                    }
                }
            }
            else
                throw new IllegalArgumentException("Invalid account credential");
        }
        else
            throw new RuntimeException("Branch manager is not ready");
    }

    public void deleteService(@NotNull AdminAccount adminAccount, @NotNull String name)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(adminAccount) != null)
            {
                if (!availableServices.contains(name))
                    throw new IllegalArgumentException("Service name does not exist");

                int sid = availableServices.indexOf(name);

                firebaseDatabase.getReference().child("availableServices").child(
                    Integer.toString(sid)).removeValue();
                firebaseDatabase.getReference().child("serviceDocuments").child(name).removeValue();
                firebaseDatabase.getReference().child("serviceDocumentsFormExtra").child(
                    name).removeValue();

                HashMap<String, String> affectedBranches = BranchManager.getInstance().getBranchesByServiceName(
                    adminAccount, name);
                firebaseDatabase.getReference().child("branchServices").child(name).removeValue();
                for (String branchName : affectedBranches.keySet())
                    firebaseDatabase.getReference().child("employeeServices").child(
                        branchName).child(name).removeValue();

                // TODO: May affect customers that was assigned to the deleted service. Need to modify this method during phase 4.
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
