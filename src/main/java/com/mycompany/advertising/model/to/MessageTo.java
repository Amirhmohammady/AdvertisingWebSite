package com.mycompany.advertising.model.to;

import java.util.Date;
import javax.persistence.*;
/**
 * Created by Amir on 10/28/2019.
 */
@Entity
public class MessageTo {
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Long ID;
    private String text;
    private String telegramlink;
    private Date startdate;
    private Date expiredate;
    private String imagename;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Long getID() {
        return ID;
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
