package team.returnteamname.servicenovigrad.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.ListView;
import android.widget.TextView;

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

public class EmployeeProcessServiceRequest extends Fragment
{
    private ListView viewCustomerInfo;
    private Button buttonAccept;
    private Button   buttonReject;
    private String serviceType;
    private String customerName;
    private EmployeeAccount employeeAccount;
    private final BranchManager branchManager = BranchManager.getInstance();

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_employee_process_service_request,
                                     container, false);
        Bundle bundle = getArguments();

        Button buttonAccept = view.findViewById(R.id.buttonEmployeeAccept);
        Button   buttonReject = view.findViewById(R.id.buttonEmployeeReject);


        viewCustomerInfo = view.findViewById(R.id.listViewCustomerInfo);
        buttonAccept = view.findViewById(R.id.buttonEmployeeAccept);
        buttonReject = view.findViewById(R.id.buttonEmployeeReject);

        String[] status = new String[]{ "Approved", "Rejected", "None" };

        if (bundle != null)
        {
            employeeAccount = (EmployeeAccount) bundle.getSerializable("account");
            serviceType = (String) bundle.getSerializable("serviceType");
            customerName = (String) bundle.getSerializable("customerName");

            try
            {
                String[] customerInfo = branchManager.getRequestCustomerSubmission(employeeAccount, customerName, serviceType);
                List<String> customerInfoList = new ArrayList<>();

                if(customerInfo != null)
                {
                    for(String info: customerInfo)
                    {
                        if(info != null)
                            customerInfoList.add(info);
                    }
                }


                DatabaseReference databaseReference = firebaseDatabase.getReference();
                databaseReference.child("branchServiceRequest").child(
                    employeeAccount.getUsername()).child(customerName).child(
                    serviceType).child("status").setValue(status[2]);

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_list_item_1,
                    customerInfoList);

                viewCustomerInfo.setAdapter(adapter);

                buttonAccept.setOnClickListener(
                    v ->
                    {
                        databaseReference.child("branchServiceRequest").child(
                            employeeAccount.getUsername()).child(customerName).child(
                            serviceType).child("status").setValue(status[0]);
                        Toast.makeText(getContext(), "Documents approved",
                                       Toast.LENGTH_SHORT).show();
                    });

                buttonReject.setOnClickListener(
                    v ->
                    {
                        databaseReference.child("branchServiceRequest").child(
                            employeeAccount.getUsername()).child(customerName).child(
                            serviceType).child("status").setValue(status[1]);
                        Toast.makeText(getContext(), "Documents Rejected",
                                       Toast.LENGTH_SHORT).show();
                    });

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
