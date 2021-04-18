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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.EmployeeAccount;
import team.returnteamname.servicenovigrad.manager.BranchManager;
import team.returnteamname.servicenovigrad.manager.ServiceManager;

public class EmployeeViewServiceRequest extends Fragment
{
    EmployeeAccount account;
    EditText selectedServiceRequest;
    ListView listViewCustomerName;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_employee_view_service_request,
                                     container, false);

        Bundle        bundle        = getArguments();
        BranchManager branchManager = BranchManager.getInstance();


        selectedServiceRequest = view.findViewById(R.id.editTextSelectServiceRequest);
        listViewCustomerName = view.findViewById(R.id.viewServiceRequest);
        Button select = view.findViewById(R.id.buttonEmployeeSelectServiceRequest);

        if (bundle != null)
        {
            account = (EmployeeAccount) bundle.getSerializable("account");
            try
            {

                String[] branchServiceName = branchManager.getEmployeeServicesRequest(account);

                ArrayList<String> branchServiceNameList = new ArrayList<>(
                    Arrays.asList(branchServiceName));
                ArrayList<String> serviceNamesList = new ArrayList<>();

                for (int i = 0; i < branchServiceNameList.size(); i++)
                {
                    serviceNamesList.add(branchServiceNameList.get(i));
                }


                String[]     serviceNames    = serviceNamesList.toArray(new String[0]);
                List<String> serviceNameList = new ArrayList<>();

                for (String serviceName : serviceNames)
                {
                    if (serviceName != null)
                        serviceNameList.add(serviceName);
                }

                if (serviceNamesList.size() == 0)
                {
                    Toast.makeText(getContext(), "No service request can be found",
                                   Toast.LENGTH_SHORT).show();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_list_item_1,
                    serviceNameList);

                listViewCustomerName.setAdapter(adapter);
                listViewCustomerName.setOnItemClickListener(
                    (parent, view1, position, id) ->
                        selectedServiceRequest.setText(serviceNameList.get(position))
                );

                select.setOnClickListener(this::onClickItem);
            }


            catch (Exception e)
            {
                Toast.makeText(getContext(), e.getMessage(),
                               Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }

        else
            throw new IllegalArgumentException("Invalid argument");

        return view;
    }

    private void onClickItem(View view)
    {
        CharSequence serviceNameSequence = selectedServiceRequest.getText();
        String       customerName;

        if (serviceNameSequence == null
            || (customerName = serviceNameSequence.toString().trim()).equals(""))
        {
            Toast.makeText(getContext(), "Field cannot be empty",
                           Toast.LENGTH_SHORT).show();
            return;
        }

        customerName = serviceNameSequence.toString().trim();
        replaceFragment(EmployeeViewServiceRequestSelectService.class, customerName);

    }

    private void replaceFragment(Class<? extends Fragment> fragmentClass, String customerName)
    {
        try
        {
            Fragment fragment = fragmentClass.newInstance();

            Bundle bundleInner = new Bundle();
            bundleInner.putSerializable("account", account);
            bundleInner.putSerializable("customerName", customerName);
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
