package team.returnteamname.servicenovigrad.service.document;

import java.util.HashMap;
import java.util.Map;

public class FormDocument extends Document
{
    private final Map<String, String> formMap;

    public FormDocument(String name, String type)
    {
        super(name, type);
        formMap = new HashMap<>();
    }

    public Map<String, String> getFormMap()
    {
        return formMap;
    }

    public void addFormItem(String key, String value)
    {
        formMap.put(key, value);
    }

    public String deleteFormItem(String key)
    {
        return formMap.remove(key);
    }

    public String getFormItem(String key)
    {
        return formMap.get(key);
    }
}
