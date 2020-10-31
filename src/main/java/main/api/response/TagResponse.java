package main.api.response;

import main.api.response.model.Tag;

import java.util.List;

public class TagResponse {
    private List<Tag> tags;

    public List<Tag> getTags() {
        return tags;
    }

    public TagResponse setTags(List<Tag> tags) {
        this.tags = tags;
        return this;
    }
}
