package main.service;

import main.api.request.CommentRequest;
import main.api.response.ResultResponse;
import main.mapper.CommentMapper;
import main.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Autowired
    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
    }

    public ResultResponse addComment(CommentRequest commentRequest) {
        Map<String, String> errors = checkComment(commentRequest);
        if (errors.isEmpty()) {
            return new ResultResponse()
                    .setId(commentRepository.save(commentMapper.toModel(commentRequest)).getId());
        } else {
            return new ResultResponse()
                    .setResult(false)
                    .setErrors(errors);
        }
    }

    private Map<String, String> checkComment(CommentRequest comment) {
        HashMap<String, String> errors = new HashMap<>(2);
        if (comment.getText().isEmpty() || comment.getText().length() < 3) {
            errors.put("title", "Заголовок не установлен");
        }
        return errors;
    }
}