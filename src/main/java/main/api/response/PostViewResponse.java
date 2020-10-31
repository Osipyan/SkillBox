package main.api.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import main.api.response.model.PostView;

import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class PostViewResponse {
    private long count;
    private List<PostView> posts;
}