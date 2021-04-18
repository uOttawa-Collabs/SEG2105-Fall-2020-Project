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
import java.util.List;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.AdminAccount;
import team.returnteamname.servicenovigrad.manager.ServiceManager;

public class AdminEditServiceFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_admin_edit_service,
                                     container, false);
        Bundle         bundle         = getArguments();
        ServiceManager serviceManager = ServiceManager.getInstance();

        ListView listViewService = view.findViewById(R.id.listViewService);
        EditText editTextService = view.findViewById(R.id.editTextService);
        Button   buttonEdit      = view.findViewById(R.id.buttonEdit);

        if (bundle != null)
        {
            AdminAccount account = (AdminAccount) bundle.getSerializable("account");
            try
            {
                String[]     serviceNames    = serviceManager.getAllServicesName(account);
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
                        editTextService.setText(serviceNameList.get(position))
                );

                buttonEdit.setOnClickListener(
                    v ->
                    {
                        CharSequence serviceNameSequence = editTextService.getText();
                        String       serviceName;

                        if (serviceNameSequence == null
                            || (serviceName = serviceNameSequence.toString().trim()).equals(""))
                        {
                            Toast.makeText(getContext(), "Field cannot be empty",
                                           Toast.LENGTH_SHORT).show();
                            return;
                        }

                        for (String name : serviceManager.getAllServicesName(account))
                        {
                            if (name != null && name.equals(serviceName))
                            {
                                try
                                {
                                    Fragment fragment = new AdminEditServiceFragmentInner();

                                    Bundle bundleInner = new Bundle();
                                    bundleInner.putSerializable("account", account);
                                    bundleInner.putSerializable("serviceName", serviceName);
                                    fragment.setArguments(bundleInner);

                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmentManager.beginTransaction().replace(R.id.layoutFragment,
                                                                               fragment).commit();
                                    return;
                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(getContext(), e.getMessage(),
                                                   Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                    return;
                                }
                            }
                        }

                        Toast.makeText(getContext(), "Service not found", Toast.LENGTH_LONG).show();
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
