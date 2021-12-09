package com.mycompany.advertising.model.to;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by Amir on 12/8/2021.
 */
@Entity
public class PersistentLoginsTo {
    @Id
    private String series;
    private String username;
    private String token;
    private Date lastUsed;

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(Date lastUsed) {
        this.lastUsed = lastUsed;
    }
}

