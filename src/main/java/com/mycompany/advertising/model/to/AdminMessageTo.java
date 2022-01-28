package com.mycompany.advertising.model.to;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Amir on 1/27/2022.
 */
@Entity
public class AdminMessageTo {
    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false)
    private Long id;
    @ManyToOne(targetEntity = UserTo.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private UserTo owner;
    private String message;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public AdminMessageTo() {
    }

    public AdminMessageTo(UserTo owner, String message, Date date) {

        this.owner = owner;
        this.message = message;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserTo getOwner() {
        return owner;
    }

    public void setOwner(UserTo owner) {
        this.owner = owner;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
