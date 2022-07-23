package com.mycompany.advertising.model.to;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Amir on 6/23/2022.
 */
@Entity
public class AdvertiseCategoryTo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToMany(targetEntity = MultiLanguageCategoryTo.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "advertiseCategory")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<MultiLanguageCategoryTo> category;
    @OneToOne(optional = true)
    @JoinColumn(nullable = true)
    private AdvertiseCategoryTo parent;
    //@ManyToMany(cascade = {CascadeType.ALL}, mappedBy = "categories", fetch = FetchType.LAZY)
    @JsonIgnore
    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<AdvertiseTo> advertiseTos;
    @JsonIgnore
    @Transient
    private Map<String, String> languagesAsMap;

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

    public List<MultiLanguageCategoryTo> getCategory() {
        return category;
    }

    public void setCategory(List<MultiLanguageCategoryTo> category) {
        this.category = category;
    }

    public AdvertiseCategoryTo getParent() {
        return parent;
    }

    public void setParent(AdvertiseCategoryTo parent) {
        this.parent = parent;
    }

    public Map<String, String> getLanguagesAsMap() {
        if (languagesAsMap == null) {
            languagesAsMap = category.stream().collect(Collectors.toMap(M -> M.getLanguage().toString(), MultiLanguageCategoryTo::getText));
            languagesAsMap.put("CatId", String.valueOf(id));
        }
        return languagesAsMap;
    }
}
