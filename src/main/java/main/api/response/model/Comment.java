package main.api.response.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Date;

@Getter
@Setter
@Accessors(chain = true)
public class Comment {
    private Integer id;
    private Long timestamp;
    private String text;
    private UserView user;
}
