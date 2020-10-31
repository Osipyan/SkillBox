package main.model;

import main.enums.ModerationStatus;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "posts")
public class Post {
    private static final String DEFAULT_STATUS = "'NEW'";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;

    @Enumerated(EnumType.STRING)
    @ColumnDefault(DEFAULT_STATUS)
    @Column(name = "moderation_status", nullable = false)
    private ModerationStatus moderationStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "moderator_id", referencedColumnName = "id")
    private User moderator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String text;

    @Column(nullable = false)
    private int view_count;

    @OneToMany(mappedBy = "tag", fetch = FetchType.LAZY)
    private List<TagToPost> tags;

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    private List<PostComment> comments;

    public int getId() {
        return id;
    }

    public Post setId(int id) {
        this.id = id;
        return this;
    }

    public boolean getActive() {
        return isActive;
    }

    public Post setActive(boolean active) {
        isActive = active;
        return this;
    }

    public ModerationStatus getModerationStatus() {
        return moderationStatus;
    }

    public Post setModerationStatus(ModerationStatus moderationStatus) {
        this.moderationStatus = moderationStatus;
        return this;
    }

    public User getModerator() {
        return moderator;
    }

    public Post setModerator(User moderator) {
        this.moderator = moderator;
        return this;
    }

    public User getUser() {
        return user;
    }

    public Post setUser(User user) {
        this.user = user;
        return this;
    }

    public Date getTime() {
        return time;
    }

    public Post setTime(Date time) {
        this.time = time;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Post setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getText() {
        return text;
    }

    public Post setText(String text) {
        this.text = text;
        return this;
    }

    public int getView_count() {
        return view_count;
    }

    public Post setView_count(int view_count) {
        this.view_count = view_count;
        return this;
    }

    public List<TagToPost> getTags() {
        return tags;
    }

    public Post setTags(List<TagToPost> tags) {
        this.tags = tags;
        return this;
    }

    public List<PostComment> getComments() {
        return comments;
    }

    public Post setComments(List<PostComment> comments) {
        this.comments = comments;
        return this;
    }
}
