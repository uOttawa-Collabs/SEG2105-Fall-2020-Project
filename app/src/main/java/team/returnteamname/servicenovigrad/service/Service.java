package team.returnteamname.servicenovigrad.service;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Service implements Serializable
{
    private String                name;
    private Map<String, Document> requiredInformation;

    public Service(String name)
    {
        this.name           = name;
        requiredInformation = new HashMap<>();
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void addRequiredInformation(String key, Document value)
    {
        requiredInformation.put(key, value);
    }

    public Document deleteRequiredInformation(String key)
    {
        return requiredInformation.remove(key);
    }

    public Document getRequiredInformation(String key)
    {
        return requiredInformation.get(key);
    }
}
