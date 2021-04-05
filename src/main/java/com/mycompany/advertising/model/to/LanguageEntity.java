package com.mycompany.advertising.model.to;

import org.springframework.data.annotation.Id;

import javax.persistence.*;

/**
 * Created by Amir on 8/19/2020.
 */
@Entity
@Table(name = "languages")
public class LanguageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column
    private String locale;

    @Column()//name = "messagekey")
    private String key;

    @Column()//name = "messagecontent")
    private String content;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //Getter & Setter

}