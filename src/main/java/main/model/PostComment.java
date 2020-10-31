package main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "post_comments")
public class PostComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "parent_id")
    private int parentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, referencedColumnName = "id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    public int getId() {
        return id;
    }

    public PostComment setId(int id) {
        this.id = id;
        return this;
    }

    public int getParentId() {
        return parentId;
    }

    public PostComment setParentId(int parentId) {
        this.parentId = parentId;
        return this;
    }

    public Post getPost() {
        return post;
    }

    public PostComment setPost(Post post) {
        this.post = post;
        return this;
    }

    public User getUser() {
        return user;
    }

    public PostComment setUser(User user) {
        this.user = user;
        return this;
    }

    public Date getTime() {
        return time;
    }

    public PostComment setTime(Date time) {
        this.time = time;
        return this;
    }

    public String getText() {
        return text;
    }

    public PostComment setText(String text) {
        this.text = text;
        return this;
    }
}
