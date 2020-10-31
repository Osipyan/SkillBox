package main.mapper;

import main.api.request.CommentRequest;
import main.model.PostComment;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper extends BaseMapper {
    public PostComment toModel(CommentRequest commentRequest) {
        return mapper.map(commentRequest, PostComment.class);
    }
}
