package team.returnteamname.servicenovigrad.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.CustomerAccount;
import team.returnteamname.servicenovigrad.manager.BranchManager;

public class CustomerRatingBranchFragment extends Fragment
{
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private       RatingBar        ratingBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_customer_rating_branch,
                                     container, false);
        Bundle        bundle        = getArguments();
        BranchManager branchManager = BranchManager.getInstance();

        ratingBar = view.findViewById(R.id.ratingBar);
        EditText editTextRateScore = view.findViewById(R.id.editTextRateScore);
        Button   buttonRate        = view.findViewById(R.id.buttonRate);

        if (bundle != null)
        {
            CustomerAccount customerAccount = (CustomerAccount) bundle.getSerializable("account");

            try
            {
                ratingBar.setOnRatingBarChangeListener(
                    (ratingBar1, rating, fromTouch) ->
                    {
                        float ratingScore = ratingBar.getRating();
                        editTextRateScore.setText(Float.toString(ratingScore));
                    });
                DatabaseReference databaseReference = firebaseDatabase.getReference();
                buttonRate.setOnClickListener(
                    v ->
                    {
                        //EmployeeAccount branchName = new EmployeeAccount();    Need change when search function done.
                        databaseReference.child("ratingScores").child("Jinemployee").child(
                            customerAccount.getUsername()).setValue(
                            editTextRateScore.getText().toString());  //Need change when search function done.
                        Toast.makeText(getContext(), "Thanks for rating",
                                       Toast.LENGTH_SHORT).show();
                        double averageScore = branchManager.getAverageRatingScores("Jinemployee");
                        databaseReference.child("branchAverageScores").child(
                            "Jinemployee").setValue(averageScore);
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
