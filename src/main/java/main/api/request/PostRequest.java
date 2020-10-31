package main.api.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
public class PostRequest {
    private Long timestamp;
    private boolean active;
    private String title;
    private String[] tags;
    private String text;
}
