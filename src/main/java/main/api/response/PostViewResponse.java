package main.api.response;

import main.api.response.model.PostView;

import java.util.List;

public class PostViewResponse {
    private long count;
    private List<PostView> posts;

    public long getCount() {
        return count;
    }

    public PostViewResponse setCount(long count) {
        this.count = count;
        return this;
    }

    public List<PostView> getPosts() {
        return posts;
    }

    public PostViewResponse setPosts(List<PostView> posts) {
        this.posts = posts;
        return this;
    }
}