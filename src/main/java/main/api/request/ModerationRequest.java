package main.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import main.enums.Decision;

public class ModerationRequest {
    @JsonProperty("post_id")
    private int postId;
    private Decision decision;

    public int getPostId() {
        return postId;
    }

    public ModerationRequest setPostId(int postId) {
        this.postId = postId;
        return this;
    }

    public Decision getDecision() {
        return decision;
    }

    public ModerationRequest setDecision(Decision decision) {
        this.decision = decision;
        return this;
    }
}
