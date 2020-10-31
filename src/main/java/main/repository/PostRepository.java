package main.repository;

import main.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query(value = "SELECT p FROM Post p WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED'")
    Page<Post> findAllActivePosts(Pageable pageable);

    @Query(value = "SELECT p FROM Post p WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND LOWER(p.text) LIKE LOWER('%?2%')")
    Page<Post> findAllActivePostsWithText(Pageable pageable, String query);

    @Query(value = "SELECT p FROM Post p WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND p.time = ?2")
    Page<Post> findAllActivePostsWithDate(Pageable pageable, Date query);

    @Query(value = "SELECT p FROM Post p WHERE p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND p.tags = ?2")
    Page<Post> findAllActivePostsWithTag(Pageable pageable, String query);

    @Query(value = "SELECT p FROM Post p WHERE p.isActive = true AND p.moderationStatus = ?2")
    Page<Post> findAllActivePostsWithModerationStatus(Pageable pageable, String query);

    @Query(value = "SELECT p FROM Post p WHERE p.moderationStatus = 'ACCEPTED' AND p.id = ?1")
    Post findPostById(int query);

    @Query(value = "UPDATE Post p SET p.moderationStatus = ?1 WHERE p.id = ?2")
    void approvePost(String status, int id);

    // ошибка Table 'skillbox.Posts' doesn't exist, что делаю не так?
    // возможно ли подобное в HQL сделать? если нет?
    @Query(value = "SELECT YEAR(time) FROM Post GROUP BY YEAR(time) ORDER BY COUNT(*)",
            nativeQuery = true)
    List<String> findYearSortedByCreatedPosts();
}
