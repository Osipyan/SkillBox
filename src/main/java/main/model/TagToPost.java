package main.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "tag2post")
public class TagToPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", nullable = false, referencedColumnName = "id")
    private Post post;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tag_id", nullable = false, referencedColumnName = "id")
    private Tag tag;

    public int getId() {
        return id;
    }

    public TagToPost setId(int id) {
        this.id = id;
        return this;
    }

    public Post getPost() {
        return post;
    }

    public TagToPost setPost(Post post) {
        this.post = post;
        return this;
    }

    public Tag getTag() {
        return tag;
    }

    public TagToPost setTag(Tag tag) {
        this.tag = tag;
        return this;
    }
}
