package team.returnteamname.servicenovigrad.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.EmployeeAccount;

public class EmployeeProcessServiceRequest extends Fragment
{
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_employee_process_service_request,
                                     container, false);
        Bundle bundle = getArguments();

        Button buttonAccept = view.findViewById(R.id.buttonEmployeeAccept);
        Button   buttonReject = view.findViewById(R.id.buttonEmployeeReject);


        String[] status = new String[]{ "Approved", "Rejected", "None" };
        if (bundle != null)
        {
            EmployeeAccount account = (EmployeeAccount) bundle.getSerializable("account");
            try
            {
                DatabaseReference databaseReference = firebaseDatabase.getReference();
                databaseReference.child("branchServiceRequest").child(
                    account.getUsername()).child("customer name").child(
                    "healthCard").setValue(status[2]);


                buttonAccept.setOnClickListener(
                    v ->
                    {
                        databaseReference.child("branchServiceRequest").child(
                            account.getUsername()).child("customer name").child(
                            "healthCard").setValue(status[0]);
                        Toast.makeText(getContext(), "Documents approved",
                                       Toast.LENGTH_SHORT).show();
                    });

                buttonReject.setOnClickListener(
                    v ->
                    {
                        databaseReference.child("branchServiceRequest").child(
                            account.getUsername()).child("customer name").child(
                            "healthCard").setValue(status[1]);
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
