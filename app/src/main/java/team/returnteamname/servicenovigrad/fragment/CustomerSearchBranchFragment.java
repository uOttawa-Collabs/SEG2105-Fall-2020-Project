package team.returnteamname.servicenovigrad.fragment;

import android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.Account;
import team.returnteamname.servicenovigrad.account.CustomerAccount;
import team.returnteamname.servicenovigrad.manager.AccountManager;
import team.returnteamname.servicenovigrad.manager.BranchManager;
import team.returnteamname.servicenovigrad.manager.ServiceManager;


public class CustomerSearchBranchFragment extends Fragment
{
    private final AccountManager accountManager = AccountManager.getInstance();
    private final BranchManager  branchManager  = BranchManager.getInstance();
    private final ServiceManager serviceManager = ServiceManager.getInstance();
    private       ListView       listViewBranch;
    private       EditText       editTextSearch;
    private       Button         buttonSearch;

    private CustomerAccount account;

    private HashSet<String>  hashSetBranchNames;
    private List<String>     listBranchNames;
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

        hashSetBranchNames = new HashSet<>();
        listBranchNames    = new ArrayList<>();

        if (bundle != null)
        {
            account = (CustomerAccount) bundle.getSerializable("account");

            arrayAdapterListView = new ItemArrayAdapter(view.getContext(),
                                                        R.layout.layout_list_view_branch_item,
                                                        listBranchNames, account);
            listViewBranch.setAdapter(arrayAdapterListView);

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

        hashSetBranchNames.clear();
        arrayAdapterListView.clear();

        try
        {
            HashMap<String, Boolean> branchNames = accountManager.getAllEmployeeAccountNames(
                account);

            for (String name : branchNames.keySet())
            {
                // Search for branch name
                if (name != null && name.matches(searchRegex))
                    addToList(name);

                // Search for working hours
                HashMap<String, String> workingHours = branchManager.getBranchWorkingHoursByUsername(
                    account, name);

                if (workingHours != null)
                {
                    for (String key : workingHours.keySet())
                    {
                        if (key != null && key.matches(searchRegex))
                            addToList(name);

                        String hours = workingHours.get(key);

                        if (hours != null && hours.matches(searchRegex))
                            addToList(name);
                    }
                }

                // Search for address
                String address = branchManager.getBranchAddressByUsername(account, name);
                if (address != null && address.matches(searchRegex))
                    addToList(name);
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

            arrayAdapterListView.notifyDataSetChanged();
        }
        catch (Exception e)
        {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void onClickItem(AdapterView<?> adapterView, View view, int i, long l)
    {
        String branchName = arrayAdapterListView.getItem(i);
        replaceFragment(CustomerSelectServiceFragment.class, branchName);
    }

    private void addToList(String branchName)
    {
        if (!hashSetBranchNames.contains(branchName))
        {
            hashSetBranchNames.add(branchName);
            arrayAdapterListView.add(branchName);
        }
    }

    private void replaceFragment(Class<? extends Fragment> fragmentClass, String branchName)
    {
        try
        {
            Fragment fragment = fragmentClass.newInstance();

            Bundle bundleInner = new Bundle();
            bundleInner.putSerializable("account", account);
            bundleInner.putSerializable("serviceName", branchName);
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
        private final Context      context;
        private final List<String> branchNameList;
        private final Account      account;

        public ItemArrayAdapter(@NonNull Context context, int resource,
                                @NotNull List<String> branchNameList, @NotNull Account account)
        {
            super(context, resource, branchNameList);
            this.context        = context;
            this.branchNameList = branchNameList;
            this.account        = account;
        }

        @SuppressLint("SetTextI18n")
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
        {
            if (convertView == null)
                convertView = LayoutInflater.from(context).inflate(
                    R.layout.layout_list_view_branch_item, parent, false);

            TextView listEntryBranchName = convertView.findViewById(R.id.listEntryBranchName);
            TextView listAddress         = convertView.findViewById(R.id.listAddress);
            TextView textViewRating      = convertView.findViewById(R.id.textViewRating);

            // Set branch name
            String branchName = getItem(position);

            if (branchName != null)
                listEntryBranchName.setText(branchName);
            else
                listEntryBranchName.setText("Unknown");

            // Set branch address
            String address = BranchManager.getInstance().getBranchAddressByUsername(account,
                                                                                    branchName);
            if (address != null)
                listAddress.setText(address);
            else
                listAddress.setText("Unknown");

            // Set branch rating
            double rating = BranchManager.getInstance().getBranchAverageRatingScoresByUsername(
                account, branchName);

            if (rating == 0)
                textViewRating.setText("Rating: No data");
            else
                textViewRating.setText("Rating: " + new DecimalFormat("#.##").format(rating));

            return convertView;
        }
    }
}
