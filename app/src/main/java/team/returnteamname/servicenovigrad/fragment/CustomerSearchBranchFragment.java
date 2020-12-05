package team.returnteamname.servicenovigrad.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.CustomerAccount;
import team.returnteamname.servicenovigrad.manager.AccountManager;
import team.returnteamname.servicenovigrad.manager.BranchManager;
import team.returnteamname.servicenovigrad.manager.ServiceManager;


public class CustomerSearchBranchFragment extends Fragment
{
    private final AccountManager  accountManager = AccountManager.getInstance();
    private final BranchManager   branchManager  = BranchManager.getInstance();
    private final ServiceManager  serviceManager = ServiceManager.getInstance();
    private       ListView        listViewBranch;
    private       EditText        editTextSearch;
    private       Button          buttonSearch;
    private       CustomerAccount account;

    private HashSet<String>  hashSetBranchNameSearchResult;
    private ItemArrayAdapter arrayAdapterListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_customer_search_branch, container, false);

        Bundle bundle = getArguments();

        listViewBranch = view.findViewById(R.id.listViewBranch);
        editTextSearch = view.findViewById(R.id.editTextSearch);
        buttonSearch   = view.findViewById(R.id.buttonSearch);

        hashSetBranchNameSearchResult = new HashSet<>();
        arrayAdapterListView          = new ItemArrayAdapter(view.getContext(),
                                                             R.layout.layout_list_view_branch_item);
        listViewBranch.setAdapter(arrayAdapterListView);

        if (bundle != null)
        {
            account = (CustomerAccount) bundle.getSerializable("account");

            try
            {
                buttonSearch.setOnClickListener(this::search);
                listViewBranch.setOnItemClickListener(this::onClickItem);
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

    private void search(View view)
    {
        CharSequence searchRegexSequence = editTextSearch.getText();
        String       searchRegex;

        if (searchRegexSequence == null
            || (searchRegex = searchRegexSequence.toString().trim()).equals(""))
        {
            Toast.makeText(getContext(), "Field cannot be empty",
                           Toast.LENGTH_SHORT).show();
            return;
        }

        hashSetBranchNameSearchResult.clear();
        arrayAdapterListView.clear();

        try
        {
            HashMap<String, Boolean> branchNames = accountManager.getAllEmployeeAccountNames(
                account);

            for (String name : branchNames.keySet())
            {
                // Search for branch name
                if (name.matches(searchRegex))
                    addToList(name);

                // Search for working hours
                HashMap<String, String> workingHours = branchManager.getBranchWorkingHoursByUsername(
                    account, name);
                for (String key : workingHours.keySet())
                {
                    if (key.matches(searchRegex))
                        addToList(name);
                    if (Objects.requireNonNull(workingHours.get(key)).matches(searchRegex))
                        addToList(name);
                }

                // TODO: Search for address

                arrayAdapterListView.notifyDataSetChanged();
            }

            // Search for types of service
            String[] availableServices = serviceManager.getAllServicesName(account);
            for (String serviceName : availableServices)
            {
                if (serviceName != null && serviceName.matches(searchRegex))
                {
                    HashMap<String, String> branchesByService = branchManager.getBranchesByServiceName(
                        account, serviceName);
                    for (String branchName : branchesByService.keySet())
                        addToList(branchName);
                }
            }
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void onClickItem(AdapterView<?> adapterView, View view, int i, long l)
    {

    }

    private void addToList(String branchName)
    {
        if (!hashSetBranchNameSearchResult.contains(branchName))
        {
            hashSetBranchNameSearchResult.add(branchName);
            arrayAdapterListView.add(branchName);
        }
    }

    private void replaceFragment(Class<? extends Fragment> fragmentClass, String serviceName)
    {
        try
        {
            Fragment fragment = fragmentClass.newInstance();

            Bundle bundleInner = new Bundle();
            bundleInner.putSerializable("account", account);
            bundleInner.putSerializable("serviceName", serviceName);
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

    // Nested class
    private static class ItemArrayAdapter extends ArrayAdapter<String>
    {
        private final Context context;

        public ItemArrayAdapter(@NonNull Context context, int resource)
        {
            super(context, resource);
            this.context = context;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            if (convertView == null)
                convertView = LayoutInflater.from(context).inflate(
                    R.layout.layout_list_view_branch_item, parent, false);

            // TODO: Set text fields

            return convertView;
        }
    }
}
