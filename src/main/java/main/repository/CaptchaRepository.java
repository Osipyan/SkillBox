package main.repository;

import main.model.CaptchaCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface CaptchaRepository extends JpaRepository<CaptchaCode, Integer> {

    @Transactional @Modifying
    @Query(value = "DELETE FROM CaptchaCode c WHERE c.time < ?1")
    void deleteAllOldCaptcha(Date currentDate);

    @Query(value = "SELECT c.code FROM CaptchaCode c WHERE c.secretCode = ?1")
    String findCodeBySecretCode(String captchaSecret);

}
