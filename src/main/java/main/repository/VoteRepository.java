package main.repository;

import main.model.PostVote;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface VoteRepository extends CrudRepository<PostVote, Integer> {
    @Query(value = "SELECT v.value FROM PostVote v WHERE v.user.id = ?1 AND v.post.id = ?2")
    Boolean findValue(int id, int postId);

    @Modifying @Transactional
    @Query(value = "UPDATE PostVote v SET v.value = ?1 WHERE v.user.id = ?2 AND v.post.id = ?3")
    void updateValue(boolean value, int userId, int postId);
}
