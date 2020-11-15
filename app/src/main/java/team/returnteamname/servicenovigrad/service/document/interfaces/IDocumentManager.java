package team.returnteamname.servicenovigrad.service.document.interfaces;

import team.returnteamname.servicenovigrad.service.document.Document;

public interface IDocumentManager
{
    Document collect();

    void verify() throws Exception;
}
