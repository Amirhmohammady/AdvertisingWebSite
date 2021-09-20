package com.mycompany.advertising.service.language;

import com.mycompany.advertising.service.api.language.MultiLanguageTipMessage;
import com.mycompany.advertising.model.dao.LanguageRepository;
import com.mycompany.advertising.model.to.LanguageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

/**
 * Created by Amir on 8/19/2020.
 */
@Service
public class MultiLanguageTipMessageImpl implements MultiLanguageTipMessage {
    @Autowired
    LanguageRepository languageRepository;

    @Override
    public void addEnglishMessage(String msg) {
        LanguageEntity languageEntity = new LanguageEntity("en_US", "home.welcome", msg);
        languageRepository.save(languageEntity);
    }

    @Override
    public void addPersianMessage(String msg) {
        LanguageEntity languageEntity = new LanguageEntity("fa", "home.welcome", msg);
        languageRepository.save(languageEntity);
    }

    @Override
    public void addMessage(String language, String msg) {
        LanguageEntity languageEntity = new LanguageEntity(language, "home.welcome", msg);
        languageRepository.save(languageEntity);
    }

    @Override
    public String getLastMessage() {
        return languageRepository.findTopByLocaleOrderByIdDesc(LocaleContextHolder.getLocale().toString()).getContent();
    }

}
