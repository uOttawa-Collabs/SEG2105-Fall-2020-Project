package team.returnteamname.servicenovigrad.service.document;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.service.document.interfaces.IDocumentManager;

public class PhotoDocumentManager implements IDocumentManager
{
    private final LayoutInflater inflater;
    private final LinearLayout   parentLayout;
    private final LinearLayout   documentLayout;
    private final EditText       editTextPhotoDocumentName;
    private final Button         buttonDeleteDocument;

    private final LinkedList<IDocumentManager> documentManagers;

    private Drawable defaultBackground = null;

    public PhotoDocumentManager(@NotNull LayoutInflater inflater,
                                @NotNull LinearLayout parentLayout,
                                @NotNull LinkedList<IDocumentManager> documentManagers)
    {
        this.inflater         = inflater;
        this.parentLayout     = parentLayout;
        this.documentManagers = documentManagers;


        this.documentLayout = (LinearLayout) inflater.inflate(R.layout.layout_photo_document,
                                                              parentLayout, false);

        editTextPhotoDocumentName = documentLayout.findViewById(R.id.editTextPhotoDocumentName);
        buttonDeleteDocument      = documentLayout.findViewById(R.id.buttonDeleteDocument);

        buttonDeleteDocument.setOnClickListener(this::deleteDocument);

        parentLayout.addView(documentLayout, documentManagers.size());
    }

    @Override
    public void setName(String name)
    {
        editTextPhotoDocumentName.setText(name);
    }

    @Override
    public Document collect()
    {
        try
        {
            if (defaultBackground == null)
                defaultBackground = editTextPhotoDocumentName.getBackground();
            else
                editTextPhotoDocumentName.setBackground(defaultBackground);

            verify();
            String documentName = editTextPhotoDocumentName.getText().toString().trim();
            return new PhotoDocument(documentName, "Photo");
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
            Toast.makeText(documentLayout.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    @Override
    public void verify() throws IllegalArgumentException
    {
        try
        {
            String documentName = editTextPhotoDocumentName.getText().toString().trim();

            if (documentName.equals(""))
            {
                editTextPhotoDocumentName.setBackgroundColor(0x30ff0000);
                throw new IllegalArgumentException(
                    "Field \"" + editTextPhotoDocumentName.getHint() + "\" cannot be empty");
            }
        }
        catch (NullPointerException e)
        {
            editTextPhotoDocumentName.setBackgroundColor(0x30ff0000);
            throw new IllegalArgumentException("Field cannot be empty", e);
        }
    }

    private void deleteDocument(View view)
    {
        documentManagers.remove(this);
        parentLayout.removeView(documentLayout);
    }
}
