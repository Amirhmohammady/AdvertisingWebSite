package com.mycompany.advertising.model.to;

import com.mycompany.advertising.model.to.enums.AdvertiseStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Amir on 10/28/2019.
 */
@Entity
public class AdvertiseTo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//AUTO
    private Long id;
    @ManyToOne(targetEntity = UserTo.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = true)//cuz cascade is restricted
    private UserTo userTo;
    @Column(length = 100)
    private String title;
    @Column(nullable = false, columnDefinition = "TEXT", length = 2048)
    private String text;
    private String webSiteLink;
    @Column(nullable = false)
    private LocalDateTime startdate;
    @Column(nullable = false)
    private LocalDateTime expiredate;
    private String imageUrl1;
    private String smallImageUrl1;
    private String imageUrl2;
    private String smallImageUrl2;
    @Enumerated(EnumType.STRING)
    private AdvertiseStatus status;

    public AdvertiseTo() {
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
