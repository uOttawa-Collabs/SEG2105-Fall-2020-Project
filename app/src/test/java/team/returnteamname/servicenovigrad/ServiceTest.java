package team.returnteamname.servicenovigrad;

import org.junit.Test;

import team.returnteamname.servicenovigrad.service.document.Document;
import team.returnteamname.servicenovigrad.service.document.FormDocument;
import team.returnteamname.servicenovigrad.service.document.PhotoDocument;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ServiceTest
{
    @Test
    public void documentTest()
    {
        Document document = new Document("testName", "testType");

        assertEquals("Check Document name", "testName",
                     document.getName());
        assertEquals("Check Document type", "testType",
                     document.getType());
    }

    @Test
    public void photoDocumentTest()
    {
        PhotoDocument photoDocument = new PhotoDocument("testName", "testType");

        assertEquals("Check PhotoDocument name", "testName",
                     photoDocument.getName());
        assertEquals("Check PhotoDocument type", "testType",
                     photoDocument.getType());
    }

    @Test
    public void formDocumentBaseTest()
    {
        final FormDocument formDocument = new FormDocument("testName", "testType");

        assertEquals("Check PhotoDocument name", "testName",
                     formDocument.getName());
        assertEquals("Check PhotoDocument type", "testType",
                     formDocument.getType());
    }

    @Test
    public void formDocumentAddItemTest()
    {
        final FormDocument formDocument = new FormDocument("testName", "testType");

        for (int i = 0; i < 100; ++i)
             formDocument.addFormItem("testKey" + i, "testValue" + i);
    }

    @Test
    public void formDocumentDeleteItemTest()
    {
        final FormDocument formDocument = new FormDocument("testName", "testType");

        for (int i = 0; i < 100; ++i)
             formDocument.addFormItem("testKey" + i, "testValue" + i);

        for (int i = 20; i < 50; ++i)
             formDocument.deleteFormItem("testKey" + i);

        for (int i = 20; i < 50; ++i)
             assertNull("Check existence of deleted items: " + i,
                        formDocument.getFormItem("testKey" + i));
    }

    @Test
    public void formDocumentGetItemTest()
    {
        final FormDocument formDocument = new FormDocument("testName", "testType");

        for (int i = 0; i < 100; ++i)
             formDocument.addFormItem("testKey" + i, "testValue" + i);

        for (int i = 20; i < 50; ++i)
             formDocument.deleteFormItem("testKey" + i);

        for (int i = 20; i < 50; ++i)
             assertNull("Check existence of deleted items: " + i,
                        formDocument.getFormItem("testKey" + i));

        for (int i = 0; i < 100; ++i)
        {
            assertEquals("Check equality of remaining items: " + i, "testValue" + i,
                         formDocument.getFormItem("testKey" + i));
            if (i == 19)
                i = 49;
        }
    }
}
