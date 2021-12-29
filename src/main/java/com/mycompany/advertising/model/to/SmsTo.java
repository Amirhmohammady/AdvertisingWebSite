package com.mycompany.advertising.model.to;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Amir on 9/8/2021.
 */
@Entity
public class SmsTo {
    private String phoneno, vrificationcode;
    private Date expiredate;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID", nullable = false)
    private Long id;

    public SmsTo() {
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getVrificationcode() {
        return vrificationcode;
    }

    public void setVrificationcode(String vrificationcode) {
        this.vrificationcode = vrificationcode;
    }

    public Date getExpiredate() {
        return expiredate;
    }

    public void setExpiredate(Date expiredate) {
        this.expiredate = expiredate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
