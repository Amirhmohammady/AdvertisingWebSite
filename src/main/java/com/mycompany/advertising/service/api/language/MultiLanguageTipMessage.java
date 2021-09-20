package com.mycompany.advertising.service.api.language;

/**
 * Created by Amir on 8/19/2020.
 */
public interface MultiLanguageTipMessage {
    public void addEnglishMessage(String msg);
    public void addPersianMessage(String msg);
    public void addMessage(String language, String msg);
    public String getLastMessage();
}
