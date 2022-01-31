package com.mycompany.advertising.model.to;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Amir on 1/27/2022.
 */
@Entity
public class AdminMessageTo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//AUTO
    @Column(name = "ID", nullable = false)
    private Long id;
    @ManyToOne(targetEntity = UserTo.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private UserTo owner;
    @Column(columnDefinition = "TEXT", length = 2048)
    private String message;
    private String title;
    private Date date;

    public AdminMessageTo() {
    }

    public AdminMessageTo(UserTo owner, String message, Date date) {

        this.owner = owner;
        this.message = message;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

    @Override
    public String toString() {
        return "AdminMessageTo{" +
                "id=" + id +
                ", owner=" + owner.getUsername() +
                ", message='" + message + '\'' +
                ", title='" + title + '\'' +
                ", date=" + date +
                '}';
    }
}
