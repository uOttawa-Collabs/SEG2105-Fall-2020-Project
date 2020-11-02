package team.returnteamname.servicenovigrad.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.AdminAccount;
import team.returnteamname.servicenovigrad.manager.ServiceManager;
import team.returnteamname.servicenovigrad.service.Document;
import team.returnteamname.servicenovigrad.service.Service;

public class AdminCreateServiceFragment extends Fragment
{
    private static final String[] DOCUMENT_TYPES = { "Form document", "Photo document" };
    private              int      documentChoice = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View   view   = inflater.inflate(R.layout.fragment_admin_create_service, container, false);
        Bundle bundle = getArguments();

        ServiceManager serviceManager = ServiceManager.getInstance();

        EditText     editTextServiceName     = view.findViewById(R.id.editTextServiceName);
        LinearLayout layoutDocumentContainer = view.findViewById(R.id.layoutDocumentContainer);
        Button       buttonCreateDocument    = view.findViewById(R.id.buttonCreateDocument);
        Button       buttonCreateService     = view.findViewById(R.id.buttonCreateService);


        if (bundle != null)
        {
            AdminAccount account = (AdminAccount) bundle.getSerializable("account");

            buttonCreateDocument.setOnClickListener(
                v -> showSingleChoiceDialog(
                    (dialog, which) -> documentChoice = which,
                    (dialog, which) ->
                    {
                        switch (documentChoice)
                        {
                            case 0:
                                createFormDocument(layoutDocumentContainer);
                                break;
                            case 1:
                                createPhotoDocument(layoutDocumentContainer);
                                break;
                            default:
                                break;
                        }
                    }
                )
            );

            /*
            buttonCreateService.setOnClickListener(
                v ->
                {
                    // Obtain service name
                    CharSequence serviceNameSequence = editTextServiceName.getText();
                    String       serviceName;

                    if (serviceNameSequence == null
                        || (serviceName = serviceNameSequence.toString().trim()).equals(""))
                    {
                        Toast.makeText(getContext(), "Service name cannot be empty",
                                       Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Check if service is empty
                    if (service.isEmpty())
                    {
                        Toast.makeText(getContext(), "Service cannot be empty",
                                       Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Assign service name
                    service.setName(serviceName);

                    try
                    {
                        serviceManager.addService(account, service);
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                });
             */
        }
        else
            throw new IllegalArgumentException("Invalid argument");



        return view;
    }

    private void createFormDocument(View view)
    {

    }

    private void createPhotoDocument(View view)
    {

    }

    private void showSingleChoiceDialog(DialogInterface.OnClickListener chooseListener,
                                        DialogInterface.OnClickListener confirmListener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Choose a document type");
        builder.setSingleChoiceItems(AdminCreateServiceFragment.DOCUMENT_TYPES, 0, chooseListener);
        builder.setPositiveButton("Confirm", confirmListener);
        builder.show();
    }
}
