package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CommentRequest {
    @JsonProperty("parent_id")
    private int parentId;
    @JsonProperty("post_id")
    private int postId;
    private String text;

    public int getParentId() {
        return parentId;
    }

    public CommentRequest setParentId(int parentId) {
        this.parentId = parentId;
        return this;
    }

    public int getPostId() {
        return postId;
    }

    public CommentRequest setPostId(int postId) {
        this.postId = postId;
        return this;
    }

    public String getText() {
        return text;
    }

    public CommentRequest setText(String text) {
        this.text = text;
        return this;
    }
}
