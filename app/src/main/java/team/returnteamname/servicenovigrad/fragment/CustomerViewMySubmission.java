package team.returnteamname.servicenovigrad.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.CustomerAccount;
import team.returnteamname.servicenovigrad.account.EmployeeAccount;

public class CustomerViewMySubmission extends Fragment
{

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_customer_view_my_submission,
                                     container, false);

        Bundle bundle = getArguments();
        CustomerAccount account;

        TextView result1 = view.findViewById(R.id.textViewResult1);
        TextView result2 = view.findViewById(R.id.textViewResult2);
        TextView result3 = view.findViewById(R.id.textViewResult3);


        if (bundle != null)
        {
            account = (CustomerAccount) bundle.getSerializable("account");
            try
            {

                DatabaseReference databaseReference = firebaseDatabase.getReference();


                result1.setText(databaseReference.child("branchServiceRequest").child("Jinemployee")
                                                 .child(account.getUsername()).child("Driver's License")
                                                 .child("status").getKey());
                result2.setText(databaseReference.child("branchServiceRequest").child("Jinemployee")
                                                 .child(account.getUsername()).child("Driver's License")
                                                 .child("status").getKey());
                result3.setText("NONE");





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






















}