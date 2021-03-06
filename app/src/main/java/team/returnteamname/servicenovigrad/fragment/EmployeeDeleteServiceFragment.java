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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.EmployeeAccount;
import team.returnteamname.servicenovigrad.manager.BranchManager;

public class EmployeeDeleteServiceFragment extends Fragment
{
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_employee_delete_service,
                                     container, false);
        Bundle        bundle        = getArguments();
        BranchManager branchManager = BranchManager.getInstance();
        ArrayAdapter<String> adapter;
        ListView listViewService = view.findViewById(R.id.listViewService);
        EditText editTextService = view.findViewById(R.id.editTextService);
        Button   buttonDelete    = view.findViewById(R.id.buttonDelete);

        if (bundle != null)
        {
            EmployeeAccount account = (EmployeeAccount) bundle.getSerializable("account");
            try
            {
                String[]     serviceNames    = branchManager.getEmployeeServices(account);
                List<String> serviceNameList = new ArrayList<>();

                if (serviceNames != null)
                {
                    for (String serviceName : serviceNames)
                    {
                        if (serviceName != null)
                            serviceNameList.add(serviceName);
                    }
                    adapter = new ArrayAdapter<>(
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
                            CharSequence      serviceNameSequence = editTextService.getText();
                            String            serviceName;
                            DatabaseReference databaseReference   = firebaseDatabase.getReference();

                            if (serviceNameSequence == null
                                || (serviceName = serviceNameSequence.toString().trim()).equals(""))
                            {
                                Toast.makeText(getContext(), "Field cannot be empty",
                                               Toast.LENGTH_SHORT).show();
                                return;
                            }

                            try
                            {
                                databaseReference.child("branchServices").child(serviceName).child(
                                    account.getUsername()).removeValue();
                                databaseReference.child("employeeServices").child(
                                    account.getUsername()).child(serviceName).removeValue();

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
                else
                {
                    Toast.makeText(getContext(), "No service can be deleted", Toast.LENGTH_SHORT).show();
                }

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
