package main.api.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class CalendarResponse {
    private List<Integer> years;
    private Map<String, Integer> posts;
}
