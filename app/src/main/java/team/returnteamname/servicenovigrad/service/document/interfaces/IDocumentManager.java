package team.returnteamname.servicenovigrad.service.document.interfaces;

import team.returnteamname.servicenovigrad.service.document.Document;

public interface IDocumentManager
{
    void setName(String name);

    Document collect();

    void verify() throws Exception;
}
