package team.returnteamname.servicenovigrad.service.document;

import java.io.Serializable;

public class Document implements Serializable
{
    private final String name;
    private final String type;

    public Document(String name, String type)
    {
        this.name = name;
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public String getType()
    {
        return type;
    }
}
