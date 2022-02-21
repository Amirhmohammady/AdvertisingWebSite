package com.mycompany.advertising.model.to;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

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
    @JoinColumn(nullable = true)//cuz cascade is restricted
    private UserTo owner;
    @OneToMany(targetEntity = UserCommentTo.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "msgowner")
    @OnDelete(action = OnDeleteAction.CASCADE)
    //@JoinColumn(name = "msgowner_id")
    //@JoinColumn(nullable = true)//it is wrong
    private List<UserCommentTo> comments;
    @Column(columnDefinition = "TEXT", length = 2048)
    private String message;
    private String title;
    private Date date;
    private int messageCnt = 0;

    public AdminMessageTo() {
    }

    public AdminMessageTo(UserTo owner, String message, Date date) {
        this();
        this.owner = owner;
        this.message = message;
        this.date = date;
    }

    public int getMessageCnt() {
        return messageCnt;
    }

    public void setMessageCnt(int messageCnt) {
        this.messageCnt = messageCnt;
    }

    public List<UserCommentTo> getComments() {
        return comments;
    }

    public void setComments(List<UserCommentTo> comments) {
        this.comments = comments;
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
