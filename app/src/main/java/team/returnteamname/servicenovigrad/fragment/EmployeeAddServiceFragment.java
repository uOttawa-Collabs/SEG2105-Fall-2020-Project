package team.returnteamname.servicenovigrad.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.AdminAccount;
import team.returnteamname.servicenovigrad.account.EmployeeAccount;

import team.returnteamname.servicenovigrad.manager.AccountManager;
import team.returnteamname.servicenovigrad.manager.BranchManager;
import team.returnteamname.servicenovigrad.manager.ServiceManager;
import team.returnteamname.servicenovigrad.service.Service;
import team.returnteamname.servicenovigrad.service.document.Document;

public class EmployeeAddServiceFragment extends Fragment
{
    private final        FirebaseDatabase              firebaseDatabase = FirebaseDatabase.getInstance();
    private ArrayList<String>                                         availableServices;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_employee_add_service,
                                     container, false);
        Bundle         bundle         = getArguments();
        BranchManager branchManager = BranchManager.getInstance();

        ListView listViewService = view.findViewById(R.id.listViewService);
        EditText editTextService = view.findViewById(R.id.editTextService);
        Button   buttonAdd    = view.findViewById(R.id.buttonAdd);


        if (bundle != null)
        {
            EmployeeAccount account = (EmployeeAccount) bundle.getSerializable("account");
            try
            {
                String[]     allServiceName = branchManager.getAllServicesName(account);
                String[]     branchServiceName = branchManager.getBranchServicesName(account);

                ArrayList<String> allServiceNameList = new ArrayList<>(Arrays.asList(allServiceName));
                ArrayList<String> branchServiceNameList = new ArrayList<>(Arrays.asList(branchServiceName));
                ArrayList<String> serviceNamesList = new ArrayList<>();

                for(int i=1; i < allServiceNameList.size(); i++)
                {
                    boolean flag = false;
                    for(int j=0; j<branchServiceNameList.size(); j++)
                    {
                        if(allServiceNameList.get(i).equals(branchServiceNameList.get(j)))
                        {
                            flag = true;
                            break;
                        }
                    }

                    if(flag == false)
                    {
                        serviceNamesList.add(allServiceNameList.get(i));
                    }
                }

                String[]     serviceNames    = (String[]) serviceNamesList.toArray(new String[0]);
                List<String> serviceNameList = new ArrayList<>();
                
                if (serviceNames != null)
                {
                    for (String serviceName : serviceNames)
                    {
                        if (serviceName != null)
                            serviceNameList.add(serviceName);
                    }
                }

                if(serviceNamesList.size() == 0)
                {
                    Toast.makeText(getContext(), "No New Service Can Be Added", Toast.LENGTH_SHORT).show();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_list_item_1,
                    serviceNameList);

                listViewService.setAdapter(adapter);
                listViewService.setOnItemClickListener(
                    (parent, view1, position, id) ->
                        editTextService.setText(serviceNameList.get(position))
                );

                buttonAdd.setOnClickListener(
                    v ->
                    {
                        int sid = availableServices == null ? 0 : availableServices.size();
                        CharSequence serviceNameSequence = editTextService.getText();
                        String       serviceName;
                        DatabaseReference databaseReference = firebaseDatabase.getReference();

                        if (serviceNameSequence == null
                            || (serviceName = serviceNameSequence.toString().trim()).equals(""))
                        {
                            Toast.makeText(getContext(), "Field cannot be empty",
                                           Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try
                        {
                            databaseReference.child("branchServices").child(serviceName).child(account.getUsername().toString()).setValue(
                                account.getUsername());
                            databaseReference.child("employeeServices").child(account.getUsername()).child(serviceName).setValue(
                                serviceName);

                            editTextService.setText("");
                            adapter.remove(serviceName);
                            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    }
                );

            }
            catch (Exception e)
            {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }

        }
        else
            throw new IllegalArgumentException("Invalid argument");

        return view;

    }
}