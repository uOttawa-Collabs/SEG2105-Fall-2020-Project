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

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.AdminAccount;
import team.returnteamname.servicenovigrad.manager.AccountManager;

public class AdminDeleteAccountFragment extends Fragment
{
    private static final AccountManager ACCOUNT_MANAGER = AccountManager.getInstance();

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
            AdminAccount account = (AdminAccount) bundle.getSerializable("account");

            try
            {
                List<String> accountList = new ArrayList<>();

                for (String username : ACCOUNT_MANAGER.getAccountUsernameList(account))
                {
                    if (username != null && !username.equals(account.getUsername()))
                        accountList.add(username);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    getContext(),
                    android.R.layout.simple_list_item_1,
                    accountList);

                listViewUsername.setAdapter(adapter);
                listViewUsername.setOnItemClickListener(
                    (parent, view1, position, id) ->
                        editTextUsername.setText(accountList.get(position))
                );

                buttonDelete.setOnClickListener(
                    v ->
                    {
                        CharSequence usernameSequence = editTextUsername.getText();
                        String       username;

                        if (usernameSequence == null
                            || (username = usernameSequence.toString().trim()).equals(""))
                        {
                            Toast.makeText(getContext(), "Field cannot be empty",
                                           Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try
                        {
                            ACCOUNT_MANAGER.deleteAccount(account, username);

                            editTextUsername.setText("");
                            adapter.remove(username);
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
