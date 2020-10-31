package main.repository;

import main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    boolean existsUserByEmail(String email);

    @Modifying @Transactional
    @Query(value = "UPDATE User SET code = ?1 WHERE email = ?2")
    void saveHash(String hash, String email);

    Optional<User> findByCode (String code);
}
