package main.api.response.model;

import java.util.Date;

public class PostView {
    private int id;
    private Date timestamp;
    private UserView user;
    private String title;
    private String announce;
    private int likeCount;
    private int dislikeCount;
    private int commentCount;
    private int viewCount;

    public int getId() {
        return id;
    }

    public PostView setId(int id) {
        this.id = id;
        return this;
    }

    public Date getTime() {
        return timestamp;
    }

    public PostView setTime(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public UserView getUser() {
        return user;
    }

    public PostView setUser(UserView user) {
        this.user = user;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PostView setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAnnounce() {
        return announce;
    }

    public PostView setAnnounce(String announce) {
        this.announce = announce;
        return this;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public PostView setLikeCount(int likeCount) {
        this.likeCount = likeCount;
        return this;
    }

    public int getDislikeCount() {
        return dislikeCount;
    }

    public PostView setDislikeCount(int dislikeCount) {
        this.dislikeCount = dislikeCount;
        return this;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public PostView setCommentCount(int commentCount) {
        this.commentCount = commentCount;
        return this;
    }

    public int getViewCount() {
        return viewCount;
    }

    public PostView setViewCount(int viewCount) {
        this.viewCount = viewCount;
        return this;
    }
}
