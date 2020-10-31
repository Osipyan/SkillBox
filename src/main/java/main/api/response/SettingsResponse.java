package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SettingsResponse {
    @JsonProperty("MULTIUSER_MODE")
    private boolean multiUserMode;
    @JsonProperty("POST_PREMODERATION")
    private boolean postPremoderation;
    @JsonProperty("STATISTICS_IS_PUBLIC")
    private boolean statisticsIsPublic;

    public boolean isMultiUserMode() {
        return multiUserMode;
    }

    public SettingsResponse setMultiUserMode(boolean multiUserMode) {
        this.multiUserMode = multiUserMode;
        return this;
    }

    public boolean isPostPremoderation() {
        return postPremoderation;
    }

    public SettingsResponse setPostPremoderation(boolean postPremoderation) {
        this.postPremoderation = postPremoderation;
        return this;
    }

    public boolean isStatisticsIsPublic() {
        return statisticsIsPublic;
    }

    public SettingsResponse setStatisticsIsPublic(boolean statisticsIsPublic) {
        this.statisticsIsPublic = statisticsIsPublic;
        return this;
    }
}
