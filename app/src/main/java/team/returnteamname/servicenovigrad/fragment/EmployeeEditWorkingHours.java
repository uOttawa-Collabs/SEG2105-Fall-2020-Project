package team.returnteamname.servicenovigrad.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.EmployeeAccount;
import team.returnteamname.servicenovigrad.manager.ServiceManager;

public class EmployeeEditWorkingHours extends Fragment
{
    private           CalendarView      calendarView;
    private final FirebaseDatabase  firebaseDatabase = FirebaseDatabase.getInstance();
    private           ArrayList<String> availableServices;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_employee_edit_working_hours,
                                     container, false);
        Bundle bundle         = getArguments();

        calendarView = (view.findViewById(R.id.calendarView));
        EditText editTextStartTime = view.findViewById(R.id.editTextStartTime);
        EditText editTextEndTime = view.findViewById(R.id.editTextEndTime);
        EditText editTextSelectedDate = view.findViewById(R.id. editTextSelectedDate);
        Button buttonSubmit = view.findViewById(R.id.buttonSubmit);

        if (bundle != null)
        {
            EmployeeAccount account = (EmployeeAccount) bundle.getSerializable("account");
            try
            {
                calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(
                                @NonNull CalendarView view,
                                int year,
                                int month,
                                int dayOfMonth)
                            {
                                String Date = year + "-" + (month + 1) + "-" + dayOfMonth;


                                editTextSelectedDate.setText(Date);
                            }
                        });

                buttonSubmit.setOnClickListener(
                    v ->
                    {
                        DatabaseReference databaseReference   = firebaseDatabase.getReference();
                        try
                        {
                            databaseReference.child("branchWorkingHours").child(
                                account.getUsername().toString()).child(editTextSelectedDate.getText().toString()).setValue(editTextStartTime.getText().toString().trim()+"-"+editTextEndTime.getText().toString().trim());

                            Toast.makeText(getContext(), "Success",
                                           Toast.LENGTH_SHORT).show();
                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(getContext(), e.getMessage(),
                                                   Toast.LENGTH_LONG).show();
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