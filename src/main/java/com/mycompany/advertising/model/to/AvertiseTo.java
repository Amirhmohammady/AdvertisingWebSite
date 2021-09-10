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
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Long id;
    private Long ownerid;
    private String text;
    private String telegramlink;
    private Date startdate;
    private Date expiredate;
    private String imagename;
    private String smallimagename;
    @Enumerated(EnumType.STRING)
    private AvertiseStatus status;
    private String info;

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

    public Long getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(Long ownerid) {
        this.ownerid = ownerid;
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
