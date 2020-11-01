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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.AdminAccount;
import team.returnteamname.servicenovigrad.manager.ServiceManager;
import team.returnteamname.servicenovigrad.service.Service;

public class AdminDeleteServiceFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_admin_delete_service,
                                     container, false);
        Bundle         bundle         = getArguments();
        ServiceManager serviceManager = ServiceManager.getInstance();

        ListView listViewService = view.findViewById(R.id.listViewService);
        EditText editTextService = view.findViewById(R.id.editTextService);
        Button   buttonDelete    = view.findViewById(R.id.buttonDelete);

        if (bundle != null)
        {
            AdminAccount account = (AdminAccount) bundle.getSerializable("account");
            try
            {
                Map<String, Service> serviceMap      = serviceManager.getAllServices(account);
                List<String>         serviceNameList = new ArrayList<>();

                for (String serviceName : serviceMap.keySet())
                {
                    if (serviceName != null)
                        serviceNameList.add(serviceName);
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

                buttonDelete.setOnClickListener(
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

                        try
                        {
                            serviceManager.deleteService(account, serviceName);

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
