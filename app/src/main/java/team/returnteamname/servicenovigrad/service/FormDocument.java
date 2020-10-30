package team.returnteamname.servicenovigrad.service;

import java.util.HashMap;
import java.util.Map;

public class FormDocument extends Document
{
    private Map<String, String> formMap;

    public FormDocument(String name, String type)
    {
        super(name, type);
        formMap = new HashMap<>();
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
