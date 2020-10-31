package main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Entity(name = "captcha_codes")
public class CaptchaCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private Date time;

    @Column(nullable = false, columnDefinition = "TINYTEXT")
    private String code;

    @Column(name = "secret_code", nullable = false, columnDefinition = "TINYTEXT")
    private String secretCode;

    public int getId() {
        return id;
    }

    public CaptchaCode setId(int id) {
        this.id = id;
        return this;
    }

    public Date getTime() {
        return time;
    }

    public CaptchaCode setTime(Date time) {
        this.time = time;
        return this;
    }

    public String getCode() {
        return code;
    }

    public CaptchaCode setCode(String code) {
        this.code = code;
        return this;
    }

    public String getSecretCode() {
        return secretCode;
    }

    public CaptchaCode setSecretCode(String secretCode) {
        this.secretCode = secretCode;
        return this;
    }
}
