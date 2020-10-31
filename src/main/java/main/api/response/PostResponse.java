package main.api.response;

import main.api.response.model.Comment;
import main.api.response.model.PostView;

import java.util.List;

public class PostResponse extends PostView {
    private boolean active;
    private String text;
    private List<Comment> comments;
    private String[] tags;

    public boolean isActive() {
        return active;
    }

    public PostResponse setActive(boolean active) {
        this.active = active;
        return this;
    }

    public String getText() {
        return text;
    }

    public PostResponse setText(String text) {
        this.text = text;
        return this;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public PostResponse setComments(List<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public String[] getTags() {
        return tags;
    }

    public PostResponse setTags(String[] tags) {
        this.tags = tags;
        return this;
    }
}
