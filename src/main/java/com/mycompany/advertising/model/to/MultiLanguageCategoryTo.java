package com.mycompany.advertising.model.to;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mycompany.advertising.model.to.enums.Language;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Created by Amir on 6/30/2022.
 */
@Entity
public class MultiLanguageCategoryTo {
    @Id
    private Long id;
    private String text;
    private Language language;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(nullable = false)
    private AdvertiseCategoryTo advertiseCategory;

    public AdvertiseCategoryTo getAdvertiseCategory() {
        return advertiseCategory;
    }

    public void setAdvertiseCategory(AdvertiseCategoryTo advertiseCategory) {
        this.advertiseCategory = advertiseCategory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Language getLanguage() {
        return language;
    }

    public void setLanguage(Language language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "{" + language + "::" + text + '}';
    }
}
