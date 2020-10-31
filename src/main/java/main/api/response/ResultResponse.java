package main.api.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class ResultResponse {
    private Boolean result;
    private Integer id;
    private Map<String, String> errors;
}
