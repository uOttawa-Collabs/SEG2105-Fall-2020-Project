package team.returnteamname.servicenovigrad.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.Account;

public class HomeFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View     view     = inflater.inflate(R.layout.fragment_home, container, false);
        TextView textView = view.findViewById(R.id.textViewGreeting);
        Bundle   bundle   = getArguments();

        if (bundle != null)
        {
            Account account = (Account) bundle.getSerializable("account");

            textView.setText("Welcome, "
                             + account.getUsername()
                             + ".\nPlease select an operation using the side navigation bar.");
        }
        else
            throw new IllegalArgumentException("Invalid argument");

        return view;
    }
}
