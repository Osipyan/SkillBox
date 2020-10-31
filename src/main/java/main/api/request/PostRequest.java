package main.api.request;

import java.util.Date;

public class PostRequest {
    private Date timestamp;
    private boolean active;
    private String title;
    private String[] tags;
    private String text;

    public Date getTimestamp() {
        return timestamp;
    }

    public PostRequest setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public PostRequest setActive(boolean active) {
        this.active = active;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public PostRequest setTitle(String title) {
        this.title = title;
        return this;
    }

    public String[] getTags() {
        return tags;
    }

    public PostRequest setTags(String[] tags) {
        this.tags = tags;
        return this;
    }

    public String getText() {
        return text;
    }

    public PostRequest setText(String text) {
        this.text = text;
        return this;
    }
}
