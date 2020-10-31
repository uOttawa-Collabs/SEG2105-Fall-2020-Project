package team.returnteamname.servicenovigrad.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.AdminAccount;
import team.returnteamname.servicenovigrad.manager.ServiceManager;
import team.returnteamname.servicenovigrad.service.Service;

public class AdminDeleteServiceFragment extends Fragment
{
    private static final ServiceManager SERVICE_MANAGER = ServiceManager.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View   view   = inflater.inflate(R.layout.fragment_admin_delete_service, container, false);
        Bundle bundle = getArguments();

        EditText editTextService = view.findViewById(R.id.editTextService);
        Button buttonDelete = view.findViewById(R.id.buttonDelete);
        ListView listViewService = view.findViewById(R.id.listViewService);

        if (bundle != null)
        {
            AdminAccount account = (AdminAccount) bundle.getSerializable("account");
            Service service =  (Service) bundle.getSerializable("service");

            try
            {
                List<String> serviceList = new ArrayList<>();

                for(String serviceName: (SERVICE_MANAGER.getAllServices(account)).keySet())
                {
                    if(serviceName != null && !serviceName.equals(service.getName()))
                        serviceList.add(serviceName);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_list_item_2,
                    serviceList);

                listViewService.setAdapter(adapter);
                listViewService.setOnItemClickListener(
                    ((parent, view1, position, id) ->
                         editTextService.setText(serviceList.get(position))
                );

                buttonDelete.setOnClickListener(
                    v ->
                    {
                        CharSequence serviceSequence = editTextService.getText();
                        String serviceName;

                        if(serviceSequence == null
                            || (serviceName = serviceSequence.toString().trim()).equals(""))
                        {
                            Toast.makeText(getContext(), "Field cannot be empty", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try
                        {
                            SERVICE_MANAGER.deleteService(account, serviceName);

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
