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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.CustomerAccount;
import team.returnteamname.servicenovigrad.manager.BranchManager;

public class CustomerSelectServiceFragment extends Fragment
{
    private final BranchManager branchManager = BranchManager.getInstance();
    private ListView listViewService;
    private EditText editTextSelect;
    private Button          buttonSelect;
    private CustomerAccount account;
    private String branchName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_customer_select_service,
                                     container, false);
        Bundle        bundle        = getArguments();

        listViewService = view.findViewById(R.id.listViewService);
        editTextSelect = view.findViewById(R.id.editTextSelect);
        buttonSelect = view.findViewById(R.id.buttonSelect);

        if (bundle != null)
        {
            account    = (CustomerAccount) bundle.getSerializable("account");
            branchName = (String) bundle.getSerializable("serviceName");

            try
            {
                String[] serviceNames = branchManager.getEmployeeServicesByUsernameOnly(branchName);
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

                listViewService.setAdapter(adapter);
                listViewService.setOnItemClickListener(
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
        String serviceType;
        CharSequence serviceNameSequence = editTextSelect.getText();

        if (serviceNameSequence == null
            || (serviceType = serviceNameSequence.toString().trim()).equals(""))
        {
            Toast.makeText(getContext(), "Field cannot be empty",
                           Toast.LENGTH_SHORT).show();
            return;
        }

        serviceType = serviceNameSequence.toString().trim();
        replaceFragment(CustomerUploadDocumentFragment.class, branchName, serviceType);
        //replaceFragment(CustomerFillFormFragment.class, branchName);
    }

    private void replaceFragment(Class<? extends Fragment> fragmentClass, String branchName, String serviceType)
    {
        try
        {
            Fragment fragment = fragmentClass.newInstance();

            Bundle bundleInner = new Bundle();
            bundleInner.putSerializable("account", account);
            bundleInner.putSerializable("serviceName", branchName);
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
