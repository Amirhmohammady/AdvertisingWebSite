package com.mycompany.advertising.model.to;

import com.mycompany.advertising.entity.AvertiseStatus;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Amir on 10/28/2019.
 */
@Entity
public class AvertiseTo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//AUTO
    @Column(name = "ID", nullable = false)
    private Long id;
    @ManyToOne(targetEntity = UserTo.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private UserTo userTo;
    private String text;
    private String telegramlink;
    private Date startdate;
    private Date expiredate;
    private String imagename;
    private String smallimagename;
    @Enumerated(EnumType.STRING)
    private AvertiseStatus status;
    private String info;

    public AvertiseTo() {
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public AvertiseStatus getStatus() {
        return status;
    }

    public void setStatus(AvertiseStatus status) {
        this.status = status;
    }

    public String getSmallimagename() {
        return smallimagename;
    }

    public void setSmallimagename(String smallimagename) {
        this.smallimagename = smallimagename;
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

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public String getTelegramlink() {
        return telegramlink;
    }

    public void setTelegramlink(String telegramlink) {
        this.telegramlink = telegramlink;
    }
}
