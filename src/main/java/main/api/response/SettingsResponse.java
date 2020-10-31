package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class SettingsResponse {
    @JsonProperty("MULTIUSER_MODE")
    private Boolean multiUserMode;
    @JsonProperty("POST_PREMODERATION")
    private Boolean postPremoderation;
    @JsonProperty("STATISTICS_IS_PUBLIC")
    private Boolean statisticsIsPublic;
}
