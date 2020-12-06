package team.returnteamname.servicenovigrad.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.CustomerAccount;

public class CustomerFillFormFragment extends Fragment
{
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    String[] customerInfo = {"First name","Last Name","DOB","address"};
    String          serviceType;
    CustomerAccount account;
    String          branchName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_customer_fill_form,
                                     container, false);
        Bundle bundle = getArguments();

        EditText[] editTextValues = new EditText[4];

        editTextValues[0] = view.findViewById(R.id.editTextCustomerServiceFirstName);
        editTextValues[1] = view.findViewById(R.id.editTextCustomerServiceLastName);
        editTextValues[2] = view.findViewById(R.id.editTextCustomerServiceAddressStreet);
        editTextValues[3] = view.findViewById(R.id.editTextCustomerServicePostalAddress);

        DatePicker   customerDOB              = view.findViewById(R.id.datePickerCustomerDOB);
        LinearLayout linearLayoutCustomerForm = view.findViewById(R.id.linearLayoutCustomerForm);
        Button       buttonSubmit             = view.findViewById(R.id.buttonCustomerFormSubmit);
        Button       buttonNext               = view.findViewById(R.id.buttonCustomerFormNext);


        if (bundle != null)
        {
            account     = (CustomerAccount) bundle.getSerializable("account");
            branchName  = (String) bundle.getSerializable("serviceName");
            serviceType = (String) bundle.getSerializable("serviceType");

            try{
                buttonSubmit.setOnClickListener(
                    v ->
                    {
                        DatabaseReference databaseReference = firebaseDatabase.getReference();
                        try{

                            if (editTextValues[0] == null
                                || editTextValues[1] == null
                                || editTextValues[2] == null
                                || editTextValues[3] == null)
                            {
                                Toast.makeText(getContext(), "All fields should be entered",
                                               Toast.LENGTH_SHORT).show();
                            }

                            else
                                {
                                    // validate address, postal code, first and last name. date of birth
                                    // then send everything to the branch
                                    if((editTextValues[0].toString().equals(account.getFirstName()))
                                    || (editTextValues[1].toString().equals( account.getLastName())))
                                    {
                                        Toast.makeText(getContext(),
                                                   "Please make sure that you use correct name",
                                                   Toast.LENGTH_SHORT).show();
                                    }



                                    String customerDofB = customerDOB.getDayOfMonth()+"-"
                                                        +customerDOB.getMonth()+"-"
                                                        +customerDOB.getYear();

                                    String address = editTextValues[2].getText().toString()+", "+editTextValues[3].getText().toString();

                                    // after validate store, editTextValues[0-3], customerOofB to database.
                                    databaseReference.child("branchServiceRequest").child(branchName).child(account.getUsername())
                                                    .child(serviceType).child("First Name").setValue(account.getFirstName());
                                    databaseReference.child("branchServiceRequest").child(branchName).child(account.getUsername())
                                                    .child(serviceType).child("Last Name").setValue(account.getLastName());
                                    databaseReference.child("branchServiceRequest").child(branchName).child(account.getUsername())
                                                    .child(serviceType).child("Address").setValue(address);
                                    databaseReference.child("branchServiceRequest").child(branchName).child(account.getUsername())
                                                     .child(serviceType).child("Date Of Birth").setValue(customerDofB);
                                    Toast.makeText(getContext(), "submission success",
                                                   Toast.LENGTH_LONG).show();
                                }
                            }

                        catch (Exception e)
                        {
                            Toast.makeText(getContext(), e.getMessage(),
                                           Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    });

                buttonNext.setOnClickListener(this::onClickItem);

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

    private void onClickItem(View view)
    {
        replaceFragment(CustomerUploadDocumentFragment.class, branchName, serviceType);
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
