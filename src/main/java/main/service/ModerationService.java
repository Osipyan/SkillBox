package main.service;

import main.api.request.ModerationRequest;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModerationService {
    private final PostRepository postRepository;

    @Autowired
    public ModerationService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Boolean approveComment(ModerationRequest entity) {
        postRepository.approvePost(entity.getDecision().getValue(), entity.getPostId());
        return true;
    }
}
