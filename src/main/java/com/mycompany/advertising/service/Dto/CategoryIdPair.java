package com.mycompany.advertising.service.Dto;

/**
 * Created by Amir on 7/5/2022.
 */
public class CategoryIdPair {
    private Long id;
    private String text;

    public CategoryIdPair(Long id, String text) {
        this.text = text;
        this.id = id;
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
}
