package com.mycompany.advertising.service.language;

/**
 * Created by Amir on 7/10/2022.
 */
public class LngManager {
    public static Language whatLanguage(String lan){
        for(Language l:Language.values()){
            if (l.name().equals(lan)) return l;
        }
        return Language.fa_IR;
    }
}
