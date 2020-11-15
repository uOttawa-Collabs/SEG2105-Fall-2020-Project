package team.returnteamname.servicenovigrad.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
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
import java.util.LinkedList;
import java.util.Map;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.account.AdminAccount;
import team.returnteamname.servicenovigrad.manager.ServiceManager;
import team.returnteamname.servicenovigrad.service.Service;
import team.returnteamname.servicenovigrad.service.document.Document;
import team.returnteamname.servicenovigrad.service.document.FormDocumentManager;
import team.returnteamname.servicenovigrad.service.document.PhotoDocumentManager;
import team.returnteamname.servicenovigrad.service.document.interfaces.IDocumentManager;

public class AdminCreateServiceFragment extends Fragment
{
    private static final String[]       DOCUMENT_TYPES  = {
        "Form document", "Photo document"
    };
    private static final ServiceManager SERVICE_MANAGER = ServiceManager.getInstance();

    private LinkedList<IDocumentManager> documentManagers;
    private int                          documentChoice = 0;
    private View                         currentView;
    private EditText                     editTextServiceName;
    private LinearLayout                 layoutDocumentContainer;
    private Button                       buttonCreateDocument;
    private Button                       buttonCreateService;
    private AdminAccount                 account;

    private Drawable defaultBackground = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        currentView = inflater.inflate(R.layout.fragment_admin_create_service, container, false);
        Bundle bundle = getArguments();


        editTextServiceName     = currentView.findViewById(R.id.editTextServiceName);
        layoutDocumentContainer = currentView.findViewById(R.id.layoutDocumentContainer);
        buttonCreateDocument    = currentView.findViewById(R.id.buttonCreateDocument);
        buttonCreateService     = currentView.findViewById(R.id.buttonCreateService);

        documentManagers = new LinkedList<>();

        if (bundle != null)
        {
            account = (AdminAccount) bundle.getSerializable("account");

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

            buttonCreateService.setOnClickListener(this::createService);
        }
        else
            throw new IllegalArgumentException("Invalid argument");

        return currentView;
    }

    private void createFormDocument(LinearLayout container)
    {
        documentManagers.add(
            new FormDocumentManager(getLayoutInflater(), container, documentManagers));
    }

    private void createPhotoDocument(LinearLayout container)
    {
        documentManagers.add(
            new PhotoDocumentManager(getLayoutInflater(), container, documentManagers));
    }

    private void createService(View view)
    {
        try
        {
            if (defaultBackground == null)
                defaultBackground = editTextServiceName.getBackground();
            else
                editTextServiceName.setBackground(defaultBackground);

            verify();

            String                serviceName = editTextServiceName.getText().toString().trim();
            Map<String, Document> map         = new HashMap<>();

            for (IDocumentManager manager : documentManagers)
            {
                Document document = manager.collect();
                if (document == null)
                    return;

                String name = document.getName();
                map.put(name, document);
            }

            Service service = new Service(serviceName, map);
            SERVICE_MANAGER.createService(account, service);

            Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void verify() throws IllegalArgumentException
    {
        try
        {
            String serviceName = editTextServiceName.getText().toString().trim();

            if (serviceName.equals(""))
            {
                editTextServiceName.setBackgroundColor(0x30ff0000);
                throw new IllegalArgumentException(
                    "Field \"" + editTextServiceName.getHint() + "\" cannot be empty");
            }

            HashMap<String, Integer> map = new HashMap<>();
            for (IDocumentManager manager : documentManagers)
            {
                if (manager != null)
                {
                    Document document = manager.collect();

                    if (document != null)
                    {
                        String name = document.getName();

                        if (map.get(name) != null)
                            throw new IllegalArgumentException("Duplicated document name found");
                        else
                            map.put(name, 1);
                    }
                }
            }
        }
        catch (NullPointerException e)
        {
            throw new IllegalArgumentException("Field cannot be empty", e);
        }
    }

    private void showSingleChoiceDialog(DialogInterface.OnClickListener chooseListener,
                                        DialogInterface.OnClickListener confirmListener)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Choose a document type");
        builder.setSingleChoiceItems(AdminCreateServiceFragment.DOCUMENT_TYPES, documentChoice,
                                     chooseListener);
        builder.setPositiveButton("Confirm", confirmListener);
        builder.show();
    }
}
