package main.mapper;

import main.api.request.CommentRequest;
import main.model.PostComment;
import main.model.Tag;
import org.springframework.stereotype.Component;

@Component
public class TagMapper extends BaseMapper {
    public PostComment toModel(Tag commentRequest) {
        return mapper.map(commentRequest, PostComment.class);
    }
}
