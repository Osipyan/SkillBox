package main.service;

import main.api.request.CommentRequest;
import main.api.response.ResultResponse;
import main.model.Post;
import main.model.PostComment;
import main.repository.CommentRepository;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final AuthService authService;

    @Autowired
    public CommentService(CommentRepository commentRepository, PostRepository postRepository, AuthService authService) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.authService = authService;
    }

    public ResultResponse addComment(CommentRequest commentRequest) {
        if (commentRequest.getText().isEmpty() || commentRequest.getText().length() < 3) {
            throw new BadRequestException(Map.of("text", "Текст комментария не задан или слишком короткий"));
        }
        Post post = postRepository.findPostById(commentRequest.getPostId());
        if (post == null) {
            throw new BadRequestException(Map.of("text", "Пост с id = " + commentRequest.getPostId() + " не найден"));
        }
        PostComment comment = commentRepository.save(toModel(commentRequest, post));
        return new ResultResponse()
                .setResult(true)
                .setId(comment.getId());
    }

    private PostComment toModel(CommentRequest commentRequest, Post post) {
        return new PostComment()
                .setParentId(commentRequest.getParentId())
                .setPost(post)
                .setText(commentRequest.getText()).setTime(new Date())
                .setUser(authService.getCurrentUser());
    }
}