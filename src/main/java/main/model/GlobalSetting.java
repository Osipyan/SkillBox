package main.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity(name = "global_settings")
public class GlobalSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String value;

    public int getId() {
        return id;
    }

    public GlobalSetting setId(int id) {
        this.id = id;
        return this;
    }

    public String getCode() {
        return code;
    }

    public GlobalSetting setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public GlobalSetting setName(String name) {
        this.name = name;
        return this;
    }

    public String getValue() {
        return value;
    }

    public GlobalSetting setValue(String value) {
        this.value = value;
        return this;
    }
}
