package main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity(name = "post_votes")
public class PostVote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, referencedColumnName = "id")
    private Post post;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false)
    private boolean value;

    public int getId() {
        return id;
    }

    public PostVote setId(int id) {
        this.id = id;
        return this;
    }

    public User getUser() {
        return user;
    }

    public PostVote setUser(User user) {
        this.user = user;
        return this;
    }

    public Post getPost() {
        return post;
    }

    public PostVote setPost(Post post) {
        this.post = post;
        return this;
    }

    public Date getTime() {
        return time;
    }

    public PostVote setTime(Date time) {
        this.time = time;
        return this;
    }

    public boolean isValue() {
        return value;
    }

    public PostVote setValue(boolean value) {
        this.value = value;
        return this;
    }
}
