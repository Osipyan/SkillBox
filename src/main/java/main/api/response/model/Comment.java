package main.api.response.model;

import java.util.Date;

public class Comment {
    private int id;
    private Date timestamp;
    private String text;
    private UserView user;

    public int getId() {
        return id;
    }

    public Comment setId(int id) {
        this.id = id;
        return this;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Comment setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String getText() {
        return text;
    }

    public Comment setText(String text) {
        this.text = text;
        return this;
    }

    public UserView getUser() {
        return user;
    }

    public Comment setUser(UserView user) {
        this.user = user;
        return this;
    }
}
