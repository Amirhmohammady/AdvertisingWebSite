package com.mycompany.advertising.model.to;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Amir on 6/23/2022.
 */
@Entity
public class AdvertiseCategoryTo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String category;
    @OneToOne
    private AdvertiseCategoryTo parent;
    private int depth;
    //@ManyToMany(cascade = {CascadeType.ALL}, mappedBy = "categories", fetch = FetchType.LAZY)
    @ManyToMany(mappedBy = "categories")
    private Set<AdvertiseTo> advertiseTos;

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public AdvertiseCategoryTo getParent() {
        return parent;
    }

    public void setParent(AdvertiseCategoryTo parent) {
        this.parent = parent;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }
}
