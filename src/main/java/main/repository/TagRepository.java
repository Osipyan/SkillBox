package main.repository;

import main.model.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends CrudRepository<Tag, Integer> {
    @Query(value = "SELECT t FROM Tag t WHERE t.name LIKE ?1")
    List<Tag> findAllTagByNameIsLike(String name);

    @Query(value = "SELECT COUNT(p.post_id) " +
            "FROM tags t JOIN tag2post p on t.id = p.tag_id " +
            "GROUP BY t.id ORDER BY COUNT(p.post_id) DESC LIMIT 1",
            nativeQuery = true)
    int findCountPostsByMostPopularTag();
}
