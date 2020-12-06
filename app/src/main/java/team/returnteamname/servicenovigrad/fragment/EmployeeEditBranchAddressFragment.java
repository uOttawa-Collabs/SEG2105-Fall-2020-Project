package team.returnteamname.servicenovigrad.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.EmployeeAccount;
import team.returnteamname.servicenovigrad.manager.BranchManager;

public class EmployeeEditBranchAddressFragment extends Fragment
{
    private final BranchManager branchManager = BranchManager.getInstance();
    private       EditText      editTextAddress;
    private       Button        buttonEditAddressSubmit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_employee_edit_branch_address,
                                     container, false);
        Bundle bundle = getArguments();

        editTextAddress         = view.findViewById(R.id.editTextAddress);
        buttonEditAddressSubmit = view.findViewById(R.id.buttonEditAddressSubmit);

        if (bundle != null)
        {
            EmployeeAccount account = (EmployeeAccount) bundle.getSerializable("account");

            String addressOriginal = branchManager.getBranchAddressByUsername(account,
                                                                              account.getUsername());
            if (addressOriginal != null)
                editTextAddress.setText(addressOriginal);

            try
            {
                buttonEditAddressSubmit.setOnClickListener(
                    v ->
                    {
                        CharSequence addressSequence = editTextAddress.getText();
                        String       address;

                        if (addressSequence == null
                            || (address = addressSequence.toString().trim()).equals(""))
                        {
                            Toast.makeText(getContext(), "Field cannot be empty",
                                           Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try
                        {
                            branchManager.updateBranchAddress(account, address);
                            Toast.makeText(getContext(), "Success",
                                           Toast.LENGTH_SHORT).show();
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
