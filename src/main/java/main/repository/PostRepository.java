package main.repository;

import main.enums.ModerationStatus;
import main.model.Post;
import main.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query(value = "SELECT p FROM Post p WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND p.time <= ?1")
    Page<Post> findPosts(Pageable pageable, Date currentDate);

    @Query(value = "SELECT p FROM Post p WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND p.time <= ?1")
    List<Post> findPosts(Date currentDate);

    @Query(value = "SELECT p FROM Post p WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND p.time <= ?1 ORDER BY p.time ASC")
    List<Post> findPostsWithDefaultSort(Date currentDate);

    @Query(value = "SELECT * FROM posts p " +
            "WHERE p.is_active = true AND p.time <= ?1 AND p.moderation_status = 'ACCEPTED'" +
            "ORDER BY (SELECT COUNT(*) FROM post_comments pc WHERE pc.post_id = p.id) DESC",
            nativeQuery = true)
    Page<Post> findPopularPosts(Pageable pageable, Date currentDate);

    @Query(value = "SELECT * FROM posts p " +
            "WHERE  p.is_active = true AND p.time <= ?1 AND p.moderation_status = 'ACCEPTED'" +
            "ORDER BY (SELECT COUNT(*) FROM post_votes pv WHERE pv.post_id = p.id AND pv.value = 1) DESC",
            nativeQuery = true)
    Page<Post> findBestPosts(Pageable pageable, Date currentDate);

    @Query(value = "SELECT p FROM Post p WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND LOWER(p.text) LIKE LOWER(?1) AND p.time <= ?2")
    Page<Post> findPostsWithText(Pageable pageable, String query, Date currentDate);

    @Query(value = "SELECT * FROM posts WHERE is_active = true AND moderation_status = 'ACCEPTED' AND DATE(time) = ?1 AND time <= ?2",
            nativeQuery = true)
    Page<Post> findPostsWithDate(Pageable pageable, String date, Date currentDate);

    @Query(value = "SELECT p FROM Post p JOIN p.tags t WHERE t.name = ?1 AND p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND p.time <= ?2")
    Page<Post> findPostsWithTag(Pageable pageable, String tag, Date currentDate);

    @Query(value = "SELECT p FROM Post p WHERE p.isActive = true AND p.moderationStatus = ?1")
    Page<Post> findPostsWithModerationStatus(Pageable pageable, ModerationStatus status);

    @Query(value = "SELECT p FROM Post p JOIN p.moderator m WHERE p.isActive = true AND p.moderationStatus = ?1 AND m.id = ?2")
    Page<Post> findPostsToModeratorWithStatus(Pageable pageable, ModerationStatus status, int userId);

    @Query(value = "SELECT p FROM Post p JOIN p.user u WHERE u.id = ?1 AND p.moderationStatus = ?2 AND p.isActive = ?3")
    Page<Post> findPostsToUser(Pageable pageable, int userId, ModerationStatus status, boolean isActive);

    @Query(value = "SELECT p FROM Post p JOIN p.user u WHERE u.id = ?1 AND p.moderationStatus = 'ACCEPTED' AND p.isActive = true  AND p.time <= ?2 ORDER BY p.time ASC")
    List<Post> findPostsToUserWithDefaultSort(int userId, Date currentDate);

    @Query(value = "SELECT p FROM Post p WHERE p.id = ?1")
    Post findPostById(int query);

    @Query(value = "select YEAR(time) FROM posts GROUP BY YEAR(time) ORDER BY COUNT(*) DESC",
            nativeQuery = true)
    List<Integer> findYearSortedByCreatedPosts();

    @Query(value = "SELECT DATE(time) day, COUNT(*) count FROM posts " +
            "WHERE YEAR(time) = ?1 AND is_active = true AND moderation_status = 'ACCEPTED' AND time <= ?2 " +
            "GROUP BY day ORDER BY count DESC ",
            nativeQuery = true)
    List<List<Object>> findCreatedPostByDayOfYears(int year, Date currentDate);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Post p SET p.moderationStatus = ?1, p.moderator = ?2 WHERE p.id = ?3")
    void moderatePost(ModerationStatus status, User moderatorId, int postId);
}
