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

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.AdminAccount;
import team.returnteamname.servicenovigrad.manager.AccountManager;

public class AdminDeleteAccountFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View   view   = inflater.inflate(R.layout.fragment_admin_delete_account, container, false);
        Bundle bundle = getArguments();

        EditText editTextUsername = view.findViewById(R.id.editTextUsername);
        Button   buttonDelete     = view.findViewById(R.id.buttonDelete);
        ListView listViewUsername = view.findViewById(R.id.listViewUsername);

        if (bundle != null)
        {
            AdminAccount   account        = (AdminAccount) bundle.getSerializable("account");
            AccountManager accountManager = AccountManager.getInstance();

            try
            {
                String[] accountList = accountManager.getAccountUsernameList(account);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                                                                  android.R.layout.simple_list_item_1,
                                                                  accountList);
                listViewUsername.setAdapter(adapter);

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
