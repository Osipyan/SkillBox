package main.repository;

import main.model.GlobalSetting;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SettingsRepository extends CrudRepository<GlobalSetting, Integer> {
    @Query(value = "SELECT s.value FROM GlobalSetting s WHERE s.code LIKE ?1")
    String findValueByCode(String code);
}
