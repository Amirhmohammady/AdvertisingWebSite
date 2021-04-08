package com.mycompany.advertising.model.to;

import javax.persistence.*;

/**
 * Created by Amir on 8/19/2020.
 */
@Entity
//@Table(name = "languages2")
public class LanguageEntity {

    //(strategy = GenerationType.AUTO)
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Long id;

    //@Column
    private String locale;
    //@Column(name = "messagekey")
    private String kkey;
    //@Column(name = "messagecontent")
    private String content;

    public LanguageEntity(String locale, String key, String content) {
        this.locale = locale;
        this.kkey = key;
        this.content = content;
    }

    public LanguageEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getKkey() {
        return kkey;
    }

    public void setKkey(String kkey) {
        this.kkey = kkey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}