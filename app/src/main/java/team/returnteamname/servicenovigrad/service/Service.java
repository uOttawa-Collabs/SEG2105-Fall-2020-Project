package team.returnteamname.servicenovigrad.service;

import java.io.Serializable;
import java.util.Map;

public class Service implements Serializable
{
    private String                name;
    private Map<String, Document> requiredInformationMap;

    public Service(String name, Map<String, Document> requiredInformationMap)
    {
        this.name                   = name;
        this.requiredInformationMap = requiredInformationMap;
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
        requiredInformationMap.put(key, value);
    }

    public Document deleteRequiredInformation(String key)
    {
        return requiredInformationMap.remove(key);
    }

    public Document getRequiredInformation(String key)
    {
        return requiredInformationMap.get(key);
    }

    public Map<String, Document> getRequiredInformationMap()
    {
        return requiredInformationMap;
    }

    public void setRequiredInformationMap(Map<String, Document> requiredInformationMap)
    {
        this.requiredInformationMap = requiredInformationMap;
    }

    public boolean isEmpty()
    {
        return requiredInformationMap.isEmpty();
    }
}
