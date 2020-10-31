package main.service;

import main.api.request.ModerationRequest;
import main.enums.Mode;
import main.enums.ModerationStatus;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ModerationService {
    private final PostRepository postRepository;
    private final AuthService authService;

    @Autowired
    public ModerationService(PostRepository postRepository, AuthService authService) {
        this.postRepository = postRepository;
        this.authService = authService;
    }

    public Boolean moderatePost(ModerationRequest entity) {
        ModerationStatus moderationStatus = entity.getDecision().equals("accept") ? ModerationStatus.ACCEPTED : ModerationStatus.DECLINED;
        postRepository.moderatePost(
                moderationStatus,
                authService.getCurrentUser(),
                entity.getPostId());
        return true;
    }
}
