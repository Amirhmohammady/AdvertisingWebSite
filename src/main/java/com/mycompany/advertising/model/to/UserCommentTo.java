package com.mycompany.advertising.model.to;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Amir on 2/1/2022.
 */
@Entity
public class UserCommentTo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//AUTO
    @Column(name = "ID", nullable = false)
    private Long id;
    @ManyToOne//(targetEntity = AdminMessageTo.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private AdminMessageTo msgowner;
    @Column(columnDefinition = "TEXT", length = 512)
    private String message;
    private String name;
    private Date date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AdminMessageTo getMsgowner() {
        return msgowner;
    }

    public void setMsgowner(AdminMessageTo msgowner) {
        this.msgowner = msgowner;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
