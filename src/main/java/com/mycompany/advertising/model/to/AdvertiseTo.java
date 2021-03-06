package com.mycompany.advertising.model.to;

import com.mycompany.advertising.model.to.enums.AdvertiseStatus;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Amir on 10/28/2019.
 */
@Entity
//@Proxy(lazy=false)
public class AdvertiseTo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//AUTO
    private Long id;
    @ManyToOne(targetEntity = UserTo.class, fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)//cuz cascade is restricted
    private UserTo userTo;
    @Column(length = 64)
    private String title;
    @Column(nullable = false, columnDefinition = "TEXT", length = 512)
    private String text;
    private String webSiteLink;
    @Column(nullable = false)
    private LocalDateTime startdate;
    @Column(nullable = false)
    private LocalDateTime expiredate;
    private String imageUrl1;
    private String smallImageUrl1;
    @Enumerated(EnumType.STRING)
    private AdvertiseStatus status;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "junction_table_advertise_category",
            joinColumns = @JoinColumn(name = "advertise_id", unique = false),
            inverseJoinColumns = @JoinColumn(name = "category_id", unique = false))
    private @Size(min = 0, max = 10)
    List<AdvertiseCategoryTo> categories;

    public AdvertiseTo() {
    }

    public List<AdvertiseCategoryTo> getCategories() {
        return categories;
    }

    public void setCategories(List<AdvertiseCategoryTo> categories) {
        this.categories = categories;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public AdvertiseStatus getStatus() {
        return status;
    }

    public void setStatus(AdvertiseStatus status) {
        this.status = status;
    }

    public String getSmallImageUrl1() {
        return smallImageUrl1;
    }

    public void setSmallImageUrl1(String smallImageUrl1) {
        this.smallImageUrl1 = smallImageUrl1;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserTo getUserTo() {
        return userTo;
    }

    public void setUserTo(UserTo userTo) {
        this.userTo = userTo;
    }

    public LocalDateTime getExpiredate() {
        return expiredate;
    }

    public void setExpiredate(LocalDateTime expiredate) {
        this.expiredate = expiredate;
    }

    public String getImageUrl1() {
        return imageUrl1;
    }

    public void setImageUrl1(String imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }

    public LocalDateTime getStartdate() {
        return startdate;
    }

    public void setStartdate(LocalDateTime startdate) {
        this.startdate = startdate;
    }

    public String getWebSiteLink() {
        return webSiteLink;
    }

    public void setWebSiteLink(String webSiteLink) {
        this.webSiteLink = webSiteLink;
    }
}
