package team.returnteamname.servicenovigrad.manager;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import team.returnteamname.servicenovigrad.account.Account;
import team.returnteamname.servicenovigrad.account.AdminAccount;
import team.returnteamname.servicenovigrad.account.EmployeeAccount;
import team.returnteamname.servicenovigrad.manager.interfaces.IManagerCallback;

public class BranchManager
{
    private static final BranchManager                 INSTANCE         = new BranchManager();
    private static final AccountManager                ACCOUNT_MANAGER  = AccountManager.getInstance();
    private final        FirebaseDatabase              firebaseDatabase = FirebaseDatabase.getInstance();
    private final        Map<String, IManagerCallback> managerCallbacks = new HashMap<>();

    private boolean                                  initialized = false;
    private HashMap<String, HashMap<String, String>> branchByService;
    private HashMap<String, HashMap<String, String>> employeeServices;
    private HashMap<String, HashMap<String, String>> branchWorkingHours;
    private HashMap<String, HashMap<String, String>> branchRatingScores;
    private HashMap<String, HashMap<String, String>> branchServiceRequest;
    private HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>> branchServiceType;
    private HashMap<String, String>                  branchAddress;
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
                        employeeServices = (HashMap<String, HashMap<String, String>>) snapshot.getValue();
                        checkIfInitialized();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        throw error.toException();
                    }
                });

            reference.child("branchWorkingHours").addValueEventListener(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        branchWorkingHours = (HashMap<String, HashMap<String, String>>) snapshot.getValue();
                        checkIfInitialized();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        throw error.toException();
                    }
                });

            reference.child("branchServices").addValueEventListener(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        branchByService = (HashMap<String, HashMap<String, String>>) snapshot.getValue();
                        checkIfInitialized();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        throw error.toException();
                    }
                });

            reference.child("ratingScores").addValueEventListener(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        branchRatingScores = (HashMap<String, HashMap<String, String>>) snapshot.getValue();
                        checkIfInitialized();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        throw error.toException();
                    }
                });

            reference.child("branchServiceRequest").addValueEventListener(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        branchServiceRequest = (HashMap<String, HashMap<String, String>>) snapshot.getValue();
                        branchServiceType = (HashMap<String, HashMap<String, HashMap<String, HashMap<String, String>>>>) snapshot.getValue();
                        checkIfInitialized();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error)
                    {
                        throw error.toException();
                    }
                });


            reference.child("branchAddress").addValueEventListener(
                new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        branchAddress = (HashMap<String, String>) snapshot.getValue();
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

    public HashMap<String, HashMap<String, String>> getAllEmployeeServices(AdminAccount account)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(account) != null)
            {
                return employeeServices;
            }
            else
                throw new IllegalArgumentException("Invalid account credential");
        }
        else
            throw new RuntimeException("Branch manager is not ready");
    }

    public String[] getEmployeeServices(EmployeeAccount account)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(account) != null)
            {
                String              name = account.getUsername();
                Map<String, String> map  = employeeServices.get(name);
                if (map != null)
                    return map.keySet().toArray(new String[0]);
                else
                    return null;
            }
            else
                throw new IllegalArgumentException("Invalid account credential");
        }
        else
            throw new RuntimeException("Branch manager is not ready");
    }

    public String[] getEmployeeServicesRequest(EmployeeAccount account)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(account) != null)
            {
                String              name = account.getUsername();
                Map<String, String> map  = branchServiceRequest.get(name);
                if (map != null)
                    return map.keySet().toArray(new String[0]);
                else
                    return null;
            }
            else
                throw new IllegalArgumentException("Invalid account credential");
        }
        else
            throw new RuntimeException("Branch manager is not ready");
    }

    public String[] getEmployeeServicesType(EmployeeAccount account, String customerName)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(account) != null)
            {
                String              EmployeeName = account.getUsername();
                HashMap<String, HashMap<String, HashMap<String, String>>> map1  = branchServiceType.get(EmployeeName);
                HashMap<String, HashMap<String, String>> map2 = map1.get(customerName);


                if (map2 != null)
                    return map2.keySet().toArray(new String[0]);
                else
                    return null;
            }
            else
                throw new IllegalArgumentException("Invalid account credential");
        }
        else
            throw new RuntimeException("Branch manager is not ready");
    }

    public String[] getRequestCustomerSubmission(EmployeeAccount account, String customerName, String serviceType)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(account) != null)
            {
                String              EmployeeName = account.getUsername();
                HashMap<String, HashMap<String, HashMap<String, String>>> map1  = branchServiceType.get(EmployeeName);
                HashMap<String, HashMap<String, String>> map2 = map1.get(customerName);
                HashMap<String, String> map3 = map2.get(serviceType);
                List<String>    customerSubmissionValue = new ArrayList<>();
                List<String>    customerSubmissionKey = new ArrayList<>();
                List<String>    customerSubmission = new ArrayList<>();

                if(map3 != null)
                {
                    for(String key: map3.keySet())
                    {
                        customerSubmissionKey.add(key);
                    }
                    for(String key: map3.keySet())
                    {
                        String value = map3.get(key);
                        customerSubmissionValue.add(value);
                    }

                    for(int i=0; i<customerSubmissionKey.size(); i++)
                    {
                        String combine = customerSubmissionKey.get(i) + ": " + customerSubmissionValue.get(i);
                        customerSubmission.add(combine);
                    }

                    return customerSubmission == null ? null : customerSubmission.toArray(new String[0]);
                }
                else
                    return null;
            }
            else
                throw new IllegalArgumentException("Invalid account credential");
        }
        else
            throw new RuntimeException("Branch manager is not ready");
    }

    public String[] getEmployeeServicesByUsername(Account account, String username)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(account) != null)
            {
                Map<String, String> map = employeeServices.get(username);
                if (map != null)
                    return map.keySet().toArray(new String[0]);
                else
                    return null;
            }
            else
                throw new IllegalArgumentException("Invalid account credential");
        }
        else
            throw new RuntimeException("Branch manager is not ready");
    }



    public String[] getEmployeeServicesByUsernameOnly(String username)
    {
        if (initialized)
        {
            Map<String, String> map = employeeServices.get(username);
            if (map != null)
                return map.keySet().toArray(new String[0]);
            else
                return null;
        }
        else
            throw new RuntimeException("Branch manager is not ready");
    }

    public HashMap<String, HashMap<String, String>> getAllEmployeeWorkingHours(AdminAccount account)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(account) != null)
            {
                return branchWorkingHours;
            }
            else
                throw new IllegalArgumentException("Invalid account credential");
        }
        else
            throw new RuntimeException("Branch manager is not ready");
    }

    public HashMap<String, String> getBranchWorkingHours(EmployeeAccount account)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(account) != null)
            {
                String name = account.getUsername();
                return branchWorkingHours.get(name);
            }
            else
                throw new IllegalArgumentException("Invalid account credential");
        }
        else
            throw new RuntimeException("Branch manager is not ready");
    }

    public HashMap<String, String> getBranchWorkingHoursByUsername(Account account, String username)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(account) != null)
            {
                return branchWorkingHours.get(username);
            }
            else
                throw new IllegalArgumentException("Invalid account credential");
        }
        else
            throw new RuntimeException("Branch manager is not ready");
    }

    public HashMap<String, HashMap<String, String>> getAllBranchByService(AdminAccount account)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(account) != null)
            {
                return branchByService;
            }
            else
                throw new IllegalArgumentException("Invalid account credential");
        }
        else
            throw new RuntimeException("Branch manager is not ready");
    }

    public HashMap<String, String> getBranchesByServiceName(Account account, String serviceName)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(account) != null)
            {
                return branchByService.get(serviceName);
            }
            else
                throw new IllegalArgumentException("Invalid account credential");
        }
        else
            throw new RuntimeException("Branch manager is not ready");
    }

    public double getBranchAverageRatingScoresByUsername(Account account, String username)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(account) != null)
            {
                Map<String, String> map    = branchRatingScores.get(username);
                ArrayList<String>   scores = new ArrayList<>();
                double              sum    = 0;

                if (map != null)
                {
                    for (String key : map.keySet())
                    {
                        String value = map.get(key);
                        scores.add(value);
                    }

                    for (int i = 0; i < scores.size(); i++)
                    {
                        double num = Double.parseDouble((scores.get(i)));
                        sum = sum + num;
                    }

                    return sum / scores.size();
                }
                else
                {
                    return 0;
                }
            }
            else
                throw new IllegalArgumentException("Invalid account credential");
        }
        else
            throw new RuntimeException("Branch manager is not ready");
    }

    public HashMap<String, String> getAllBranchAddress(AdminAccount account)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(account) != null)
            {
                return branchAddress;
            }
            else
                throw new IllegalArgumentException("Invalid account credential");
        }
        else
            throw new RuntimeException("Branch manager is not ready");

    }

    public String getBranchAddressByUsername(Account account, String branchName)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(account) != null)
            {
                if (branchAddress != null)
                    return branchAddress.get(branchName);
                else
                    return null;
            }
            else
                throw new IllegalArgumentException("Invalid account credential");
        }
        else
            throw new RuntimeException("Branch manager is not ready");
    }

    public void updateBranchAddress(EmployeeAccount account, String address)
    {
        if (initialized)
        {
            if (ACCOUNT_MANAGER.verifyAccount(account) != null)
            {
                firebaseDatabase.getReference().child("branchAddress").child(
                    account.getUsername()).setValue(address);
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
