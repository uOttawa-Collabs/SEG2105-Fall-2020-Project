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
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.CustomerAccount;
import team.returnteamname.servicenovigrad.account.EmployeeAccount;
import team.returnteamname.servicenovigrad.manager.BranchManager;
import team.returnteamname.servicenovigrad.manager.ServiceManager;

public class EmployeeViewServiceRequestSelectService extends Fragment
{
    private final BranchManager   branchManager = BranchManager.getInstance();
    private       ListView        listViewServiceType;
    private       EditText        editTextSelect;
    private Button          buttonSelect;
    private EmployeeAccount employeeAccount;
    private String customerName;
    private String          branchName;
    String serviceType;

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_employee_view_request_select_service_type,
                                     container, false);

        Bundle        bundle        = getArguments();
        BranchManager branchManager = BranchManager.getInstance();

        listViewServiceType = view.findViewById(R.id.listViewService);
        editTextSelect = view.findViewById(R.id.editTextSelect);
        buttonSelect       = view.findViewById(R.id.buttonSelect);

        if (bundle != null)
        {
            employeeAccount = (EmployeeAccount) bundle.getSerializable("account");
            customerName = (String) bundle.getSerializable("customerName");

            try
            {
                String[] serviceNames = branchManager.getEmployeeServicesType(employeeAccount, customerName);
                List<String> serviceNameList = new ArrayList<>();

                if (serviceNames != null)
                {
                    for (String serviceName : serviceNames)
                    {
                        if (serviceName != null)
                            serviceNameList.add(serviceName);
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_list_item_1,
                    serviceNameList);

                listViewServiceType.setAdapter(adapter);
                listViewServiceType.setOnItemClickListener(
                    (parent, view1, position, id) ->
                        editTextSelect.setText(serviceNameList.get(position))
                );

                buttonSelect.setOnClickListener(this::onClickItem);

            }
            catch(Exception e)
            {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
        else
            throw new IllegalArgumentException("Invalid argument");

        return view;

    }

    private void onClickItem(View view)
    {

        CharSequence serviceNameSequence = editTextSelect.getText();

        if (serviceNameSequence == null
            || (serviceType = serviceNameSequence.toString().trim()).equals(""))
        {
            Toast.makeText(getContext(), "Field cannot be empty",
                           Toast.LENGTH_SHORT).show();
            return;
        }

        serviceType = serviceNameSequence.toString().trim();

        replaceFragment(EmployeeProcessServiceRequest.class, branchName, serviceType);

    }

    private void replaceFragment(Class<? extends Fragment> fragmentClass, String accountName, String serviceType)
    {
        try
        {
            Fragment fragment = fragmentClass.newInstance();

            Bundle bundleInner = new Bundle();
            bundleInner.putSerializable("account", employeeAccount);
            bundleInner.putSerializable("customerName", customerName);
            bundleInner.putSerializable("serviceType", serviceType);
            fragment.setArguments(bundleInner);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.layoutFragment,
                                                       fragment).commit();
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), e.getMessage(),
                           Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}
