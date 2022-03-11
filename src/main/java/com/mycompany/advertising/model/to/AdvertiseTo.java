package com.mycompany.advertising.model.to;

import com.mycompany.advertising.entity.AdvertiseStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * Created by Amir on 10/28/2019.
 */
@Entity
public class AdvertiseTo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//AUTO
    @Column(nullable = false)
    private Long id;
    @ManyToOne(targetEntity = UserTo.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = true)//cuz cascade is restricted
    private UserTo userTo;
    @Column(length = 100)
    private String title;
    @Column(columnDefinition = "TEXT", length = 2048)
    private String text;
    private String webSiteLink;
    private Date startdate;
    private Date expiredate;
    private String imageUrl;
    private String smallImageUrl;
    private String imageUrl2;
    private String smallImageUrl2;
    @Enumerated(EnumType.STRING)
    private AdvertiseStatus status;
    private String info;
    @Column(nullable = false)
    private LocalDateTime date;

    public AdvertiseTo() {
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getSmallImageUrl2() {
        return smallImageUrl2;
    }

    public void setSmallImageUrl2(String smallImageUrl2) {
        this.smallImageUrl2 = smallImageUrl2;
    }

    public String getImageUrl2() {
        return imageUrl2;
    }

    public void setImageUrl2(String imageUrl2) {
        this.imageUrl2 = imageUrl2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public AdvertiseStatus getStatus() {
        return status;
    }

    public void setStatus(AdvertiseStatus status) {
        this.status = status;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public void setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
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

    public Date getExpiredate() {
        return expiredate;
    }

    public void setExpiredate(Date expiredate) {
        this.expiredate = expiredate;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public String getWebSiteLink() {
        return webSiteLink;
    }

    public void setWebSiteLink(String webSiteLink) {
        this.webSiteLink = webSiteLink;
    }
}
