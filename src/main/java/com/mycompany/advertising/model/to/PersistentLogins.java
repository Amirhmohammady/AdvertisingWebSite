package com.mycompany.advertising.model.to;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Created by Amir on 12/8/2021.
 */
@Entity
public class PersistentLogins {
    @Id
    private String series;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime lastUsed;

    public PersistentLogins() {
    }

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

    public LocalDateTime getLastUsed() {
        return lastUsed;
    }

    public void setLastUsed(LocalDateTime lastUsed) {
        this.lastUsed = lastUsed;
    }
}

