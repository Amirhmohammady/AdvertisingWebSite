package com.mycompany.advertising.model.to;

import com.mycompany.advertising.model.to.enums.AdvertiseStatus;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.net.URL;
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
    @Size(max = 64, message = "title excited than 64 characters")
    @Size(min = 5, message = "title lenght should be at least 5 chars")
    @Column(length = 64)
    private String title;
    @Size(max = 512, message = "text excited than 512 characters")
    @Column(nullable = false, columnDefinition = "TEXT", length = 512)
    private String text;
    @NotNull(message = "you should put a valid URL")
    @Column(unique = true, nullable = false)
    private URL webSiteLink;
    @Column(nullable = false)
    private LocalDateTime startdate;
    private LocalDateTime expiredate;
    private URL imageUrl1;
    private URL smallImageUrl1;
    @Enumerated(EnumType.STRING)
    private AdvertiseStatus status;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "junction_table_advertise_category",
            joinColumns = @JoinColumn(name = "advertise_id", unique = false),
            inverseJoinColumns = @JoinColumn(name = "category_id", unique = false))
    @Size(min = 0, max = 10)
    private List<AdvertiseCategoryTo> categories;

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

    public URL getSmallImageUrl1() {
        return smallImageUrl1;
    }

    public void setSmallImageUrl1(URL smallImageUrl1) {
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

    public URL getImageUrl1() {
        return imageUrl1;
    }

    public void setImageUrl1(URL imageUrl1) {
        this.imageUrl1 = imageUrl1;
    }

    public LocalDateTime getStartdate() {
        return startdate;
    }

    public void setStartdate(LocalDateTime startdate) {
        this.startdate = startdate;
    }

    public URL getWebSiteLink() {
        return webSiteLink;
    }

    public void setWebSiteLink(URL webSiteLink) {
        this.webSiteLink = webSiteLink;
    }

    @Override
    public String toString() {
        return "AdvertiseTo{" +
                "id=" + id +
                ", userTo=" + (userTo != null ? userTo.getUsername() : "null") +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", webSiteLink=" + webSiteLink +
                ", startdate=" + startdate +
                ", expiredate=" + expiredate +
                ", imageUrl1=" + imageUrl1 +
                ", smallImageUrl1=" + smallImageUrl1 +
                ", status=" + status +
                ", categories=" + categories +
                '}';
    }
/*public List<String> getCatagoriesByLangguage(Language language){
         return categories.stream().map(c->c.getLanguagesAsMap().get(language.toString())).collect(Collectors.toList());
     }*/
}
