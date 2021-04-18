package team.returnteamname.servicenovigrad.service;

import java.io.Serializable;
import java.util.Map;

import team.returnteamname.servicenovigrad.service.document.Document;

public class Service implements Serializable
{
    private final String                name;
    private final Map<String, Document> documentMap;

    public Service(String name, Map<String, Document> documentMap)
    {
        this.name        = name;
        this.documentMap = documentMap;
    }

    public String getName()
    {
        return name;
    }

    public Map<String, Document> getDocumentMap()
    {
        return documentMap;
    }
}
