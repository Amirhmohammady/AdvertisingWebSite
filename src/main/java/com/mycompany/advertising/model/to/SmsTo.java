package com.mycompany.advertising.model.to;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Created by Amir on 9/8/2021.
 */
@Entity
public class SmsTo {
    private String phoneno, vrificationcode;
    private Date expiredate;

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
}
