package team.returnteamname.servicenovigrad.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.EmployeeAccount;

public class EmployeeEditWorkingHours extends Fragment
{
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private       CalendarView     calendarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_employee_edit_working_hours,
                                     container, false);
        Bundle bundle = getArguments();

        calendarView = (view.findViewById(R.id.calendarView));
        EditText editTextStartTime    = view.findViewById(R.id.editTextStartTime);
        EditText editTextEndTime      = view.findViewById(R.id.editTextEndTime);
        EditText editTextSelectedDate = view.findViewById(R.id.editTextSelectedDate);
        Button   buttonSubmit         = view.findViewById(R.id.buttonSubmit);

        if (bundle != null)
        {
            EmployeeAccount account = (EmployeeAccount) bundle.getSerializable("account");
            try
            {
                calendarView.setOnDateChangeListener(
                    (view1, year, month, dayOfMonth) ->
                    {

                        String Date = year + "-" + (month + 1) + "-" + dayOfMonth;


                        editTextSelectedDate.setText(Date);
                    });

                buttonSubmit.setOnClickListener(
                    v ->
                    {
                        DatabaseReference databaseReference = firebaseDatabase.getReference();
                        try
                        {
                            if (editTextSelectedDate == null
                                || editTextEndTime == null
                                || editTextStartTime == null)
                            {
                                Toast.makeText(getContext(), "All fields should be entered",
                                               Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String startTime = editTextStartTime.getText().toString().trim();
                                String endTime   = editTextEndTime.getText().toString().trim();


                                String[] startTimeSplit = startTime.split(":");
                                String[] endTimeSplit   = endTime.split(":");

                                int startHour = Integer.parseInt(startTimeSplit[0]);
                                int startMin  = Integer.parseInt(startTimeSplit[1]);
                                int endHour   = Integer.parseInt(endTimeSplit[0]);
                                int endMin    = Integer.parseInt(endTimeSplit[1]);


                                if (startHour > 24
                                    || startMin > 60
                                    || endHour > 24
                                    || endMin > 60
                                    ||
                                    startHour < 0
                                    || startMin < 0
                                    || endHour < 0
                                    || endMin < 0)
                                {
                                    Toast.makeText(getContext(), "Please enter a valid time",
                                                   Toast.LENGTH_SHORT).show();
                                }
                                else if (startHour > endHour || ((startHour == endHour) && (startMin
                                                                                            > endMin)))
                                {
                                    Toast.makeText(getContext(),
                                                   "End time should be bigger than start time",
                                                   Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    databaseReference.child("branchWorkingHours").child(
                                        account.getUsername()).child(
                                        editTextSelectedDate.getText().toString()).setValue(
                                        startTime + "-" + endTime);

                                    Toast.makeText(getContext(), "Success",
                                                   Toast.LENGTH_SHORT).show();
                                }

                            }

                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getContext(), e.getMessage(),
                                           Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                    });

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
