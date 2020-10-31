package main.service;

import main.api.response.SettingsResponse;
import main.model.GlobalSetting;
import main.repository.SettingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SettingsService {
    private SettingsRepository settingsRepository;

    @Autowired
    public SettingsService(SettingsRepository settingsRepository) {
        this.settingsRepository = settingsRepository;
    }

    public SettingsResponse getSettings() {
        SettingsResponse settingsResponse = new SettingsResponse();
        settingsRepository.findAll().forEach(setting -> {
            switch (setting.getCode()) {
                case "MULTIUSER_MODE":
                    settingsResponse.setMultiUserMode("YES".equals(setting.getValue()));
                    break;
                case "POST_PREMODERATION":
                    settingsResponse.setPostPremoderation("YES".equals(setting.getValue()));
                    break;
                case "STATISTICS_IS_PUBLIC":
                    settingsResponse.setStatisticsIsPublic("YES".equals(setting.getValue()));
            }
        });
        return settingsResponse;
    }

    public void saveSettings(SettingsResponse request) {
        Iterable<GlobalSetting> settings = settingsRepository.findAll();
        settings.forEach(setting -> {
            switch (setting.getCode()) {
                case "MULTIUSER_MODE":
                    setting.setValue(request.getMultiUserMode() ? "YES" : "NO");
                    break;
                case "POST_PREMODERATION":
                    setting.setValue(request.getPostPremoderation() ? "YES" : "NO");
                    break;
                case "STATISTICS_IS_PUBLIC":
                    setting.setValue(request.getStatisticsIsPublic() ? "YES" : "NO");
            }
        });
        settingsRepository.saveAll(settings);
    }
}