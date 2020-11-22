package team.returnteamname.servicenovigrad.service.document;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import team.returnteamname.servicenovigrad.R;
import team.returnteamname.servicenovigrad.service.document.interfaces.IDocumentManager;

public class FormDocumentManager implements IDocumentManager
{
    private final LayoutInflater inflater;
    private final LinearLayout   parentLayout;
    private final LinearLayout   documentLayout;

    private final EditText editTextFormDocumentName;
    private final Button   buttonCreateItem;
    private final Button   buttonDeleteDocument;

    private final LinkedList<IDocumentManager>        documentManagers;
    private final LinkedList<FormDocumentItemManager> formDocumentItemManagers;

    public FormDocumentManager(@NotNull LayoutInflater inflater,
                               @NotNull LinearLayout parentLayout,
                               @NotNull LinkedList<IDocumentManager> documentManagers)
    {
        this.inflater         = inflater;
        this.parentLayout     = parentLayout;
        this.documentManagers = documentManagers;

        this.documentLayout = (LinearLayout) inflater.inflate(R.layout.layout_form_document,
                                                              parentLayout, false);

        editTextFormDocumentName = documentLayout.findViewById(R.id.editTextFormDocumentName);
        buttonCreateItem         = documentLayout.findViewById(R.id.buttonCreateItem);
        buttonDeleteDocument     = documentLayout.findViewById(R.id.buttonDeleteDocument);


        buttonCreateItem.setOnClickListener(this::createItem);
        buttonDeleteDocument.setOnClickListener(this::deleteDocument);

        formDocumentItemManagers = new LinkedList<>();

        parentLayout.addView(documentLayout, documentManagers.size());

    }

    private void createItem(View view)
    {
        formDocumentItemManagers.add(
            new FormDocumentItemManager(inflater, documentLayout, formDocumentItemManagers));
    }

    private void deleteDocument(View view)
    {
        documentManagers.remove(this);
        parentLayout.removeView(documentLayout);
    }

    @Override
    public void setName(String name)
    {
        editTextFormDocumentName.setText(name);
    }

    @Override
    public FormDocument collect()
    {
        try
        {
            verify();

            String       documentName = editTextFormDocumentName.getText().toString().trim();
            FormDocument document     = new FormDocument(documentName, "Form");

            for (FormDocumentItemManager manager : formDocumentItemManagers)
            {
                Map.Entry<String, String> entry = manager.collect();

                if (entry != null)
                {
                    if (document.getFormItem(entry.getKey()) != null)
                        throw new IllegalArgumentException("Found duplicated form key field");

                    document.addFormItem(entry.getKey(), entry.getValue());
                }
                else
                    return null;
            }

            return document;
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
            String documentName = editTextFormDocumentName.getText().toString().trim();

            if (documentName.equals(""))
            {
                editTextFormDocumentName.setBackgroundColor(0x30ff0000);
                throw new IllegalArgumentException(
                    "Field \"" + editTextFormDocumentName.getHint() + "\" cannot be empty");
            }
        }
        catch (NullPointerException e)
        {
            editTextFormDocumentName.setBackgroundColor(0x30ff0000);
            throw new IllegalArgumentException("Field cannot be empty", e);
        }
    }

    public void setFormContents(HashMap<String, String> form)
    {
        for (String itemName : form.keySet())
        {
            FormDocumentItemManager manager = new FormDocumentItemManager(inflater, documentLayout,
                                                                          formDocumentItemManagers);
            manager.setItemName(itemName);
            manager.setItemRegex(form.get(itemName));

            formDocumentItemManagers.add(manager);
        }
    }

    // Nested classes
    private static class FormDocumentItemManager
    {
        private final LayoutInflater   inflater;
        private final LinearLayout     parentLayout;
        private final ConstraintLayout documentItemLayout;

        private final EditText editTextItem;
        private final EditText editTextRegex;
        private final Button   buttonDeleteItem;

        private final LinkedList<FormDocumentItemManager> formDocumentItemManagers;

        private Drawable defaultBackground = null;

        public FormDocumentItemManager(@NotNull LayoutInflater inflater,
                                       @NotNull LinearLayout parentLayout,
                                       @NotNull
                                           LinkedList<FormDocumentItemManager> formDocumentItemManagers)
        {
            this.inflater                 = inflater;
            this.parentLayout             = parentLayout;
            this.formDocumentItemManagers = formDocumentItemManagers;

            documentItemLayout = (ConstraintLayout) inflater.inflate(
                R.layout.layout_form_document_row,
                parentLayout, false);

            editTextItem     = documentItemLayout.findViewById(R.id.editTextItem);
            editTextRegex    = documentItemLayout.findViewById(R.id.editTextRegex);
            buttonDeleteItem = documentItemLayout.findViewById(R.id.buttonDeleteItem);

            buttonDeleteItem.setOnClickListener(this::deleteItem);

            parentLayout.addView(documentItemLayout, formDocumentItemManagers.size() + 1);
        }

        public void setItemName(String name)
        {
            editTextItem.setText(name);
        }

        public void setItemRegex(String regex)
        {
            editTextRegex.setText(regex);
        }

        public Map.Entry<String, String> collect()
        {
            try
            {
                if (defaultBackground == null)
                    defaultBackground = editTextItem.getBackground();
                else
                {
                    editTextItem.setBackground(defaultBackground);
                    editTextRegex.setBackground(defaultBackground);
                }

                verify();
                String item  = editTextItem.getText().toString().trim();
                String regex = editTextRegex.getText().toString().trim();

                return new Entry<>(item, regex);
            }
            catch (RuntimeException e)
            {
                e.printStackTrace();
                Toast.makeText(documentItemLayout.getContext(), e.getMessage(),
                               Toast.LENGTH_LONG).show();
                return null;
            }
        }

        public void verify() throws RuntimeException
        {
            try
            {
                String item  = editTextItem.getText().toString().trim();
                String regex = editTextRegex.getText().toString().trim();

                if (item.equals(""))
                {
                    editTextItem.setBackgroundColor(0x30ff0000);
                    throw new IllegalArgumentException(
                        "Field \"" + editTextItem.getHint() + "\" cannot be empty");
                }

                if (regex.equals(""))
                {
                    editTextRegex.setBackgroundColor(0x30ff0000);
                    throw new IllegalArgumentException(
                        "Field \"" + editTextRegex.getHint() + "\" cannot be empty");
                }

                try
                {
                    Pattern.compile(regex);
                }
                catch (PatternSyntaxException e)
                {
                    editTextRegex.setBackgroundColor(0x30ff0000);
                    throw e;
                }
            }
            catch (NullPointerException e)
            {
                throw new IllegalArgumentException("Field cannot be empty", e);
            }
        }

        private void deleteItem(View view)
        {
            formDocumentItemManagers.remove(this);
            parentLayout.removeView(documentItemLayout);
        }

        public static class Entry<K, V> implements Map.Entry<K, V>
        {
            private final K key;
            private       V value;

            public Entry(K key, V value)
            {
                this.key   = key;
                this.value = value;
            }

            @Override
            public K getKey()
            {
                return key;
            }

            @Override
            public V getValue()
            {
                return value;
            }

            @Override
            public V setValue(V value)
            {
                this.value = value;
                return value;
            }
        }
    }
}
