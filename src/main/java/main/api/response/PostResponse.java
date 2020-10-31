package main.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import main.api.response.model.Comment;
import main.api.response.model.UserName;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class PostResponse {
    private boolean active;
    private String text;
    private List<Comment> comments;
    private String[] tags;
    private Integer id;
    @JsonProperty("timestamp")
    private Long time;
    private UserName user;
    private String title;
    private Integer likeCount;
    private Integer dislikeCount;
    private Integer commentCount;
    private Integer viewCount;
}
