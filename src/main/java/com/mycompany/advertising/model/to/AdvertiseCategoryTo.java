package com.mycompany.advertising.model.to;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.mycompany.advertising.service.language.Language;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Map;
import java.util.Set;

/**
 * Created by Amir on 6/23/2022.
 */
@Entity
public class AdvertiseCategoryTo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(optional = true)
    @JoinColumn(nullable = true)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    @JsonIdentityReference(alwaysAsId = true)
    private AdvertiseCategoryTo parent;
    @ElementCollection
    @JoinColumn()//reqired for OnDelete
    @OnDelete(action = OnDeleteAction.CASCADE)
    Map<Language, String> category;
    //@ManyToMany(cascade = {CascadeType.ALL}, mappedBy = "categories", fetch = FetchType.LAZY)
    @JsonIgnore
    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<AdvertiseTo> advertiseTos;

    @Override
    public String toString() {
        return "AdvertiseCategoryTo{" +
                "id=" + id +
                ", category=" + category +
                ", parent=" + (parent != null ? parent.getId() : null) +
                '}';
    }

    public Set<AdvertiseTo> getAdvertiseTos() {
        return advertiseTos;
    }

    public void setAdvertiseTos(Set<AdvertiseTo> advertiseTos) {
        this.advertiseTos = advertiseTos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<Language, String> getCategory() {
        return category;
    }

    public void setCategory(Map<Language, String> category) {
        this.category = category;
    }

    public AdvertiseCategoryTo getParent() {
        return parent;
    }

    public void setParent(AdvertiseCategoryTo parent) {
        this.parent = parent;
    }

}

    /*public Map<String, String> getLanguagesAsMap() {
        if (languagesAsMap == null) {
            languagesAsMap = category.stream().collect(Collectors.toMap(M -> M.getLanguage().toString(), MultiLanguageCategoryTo::getText));
            languagesAsMap.put("CatId", String.valueOf(id));
        }
        return languagesAsMap;
    }*/

