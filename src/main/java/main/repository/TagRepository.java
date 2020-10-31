package main.repository;

import main.model.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TagRepository extends CrudRepository<Tag, Integer> {
    @Query(value = "SELECT t FROM Tag t JOIN t.posts p WHERE t.name LIKE ?1 AND p.isActive = true AND p.moderationStatus = 'ACCEPTED' AND p.time <= ?2 GROUP BY t.id")
    List<Tag> findAllTagByNameIsLike(String name, Date currentDate);

    @Query(value = "SELECT COUNT(p.id) FROM tags t JOIN tag2post tp on t.id = tp.tag_id JOIN posts p ON p.id = tp.post_id " +
            "WHERE p.is_active = true AND p.moderation_status = 'ACCEPTED' AND p.time <= ?1 " +
            "GROUP BY t.id ORDER BY COUNT(p.id) DESC LIMIT 1",
            nativeQuery = true)
    int findCountPostsByMostPopularTag(Date currentDate);

    @Query(value = "SELECT t FROM Tag t WHERE t.name = ?1")
    Tag findTagByName(String name);
}
