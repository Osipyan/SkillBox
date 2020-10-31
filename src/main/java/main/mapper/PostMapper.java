package main.mapper;

import main.api.request.PostRequest;
import main.api.response.PostResponse;
import main.api.response.model.PostView;
import main.model.Post;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class PostMapper extends BaseMapper {

    @PostConstruct
    public void setupMapper() {
        mapper.createTypeMap(PostRequest.class, Post.class)
                .addMapping(PostRequest::getTimestamp, Post::setTime)
                .addMapping(PostRequest::isActive, Post::setActive);
    }

    public List<PostView> toViewList(List<Post> entityList) {
        return mapList(entityList, PostView.class);
    }

    public PostResponse toPostResponse(Post post) {
        return mapper.map(post, PostResponse.class);
    }

    public Post toModel(PostRequest post) {
        return mapper.map(post, Post.class);
    }

}
