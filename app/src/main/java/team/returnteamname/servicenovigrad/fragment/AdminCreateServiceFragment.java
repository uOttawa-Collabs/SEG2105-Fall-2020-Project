package team.returnteamname.servicenovigrad.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.AdminAccount;

public class AdminCreateServiceFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View   view   = inflater.inflate(R.layout.fragment_admin_create_service, container, false);
        Bundle bundle = getArguments();

        if (bundle != null)
        {
            AdminAccount account = (AdminAccount) bundle.getSerializable("account");

        }
        else
            throw new IllegalArgumentException("Invalid argument");

        return view;
    }
}
