package main.service;

import main.api.request.PostRequest;
import main.api.response.PostResponse;
import main.api.response.PostViewResponse;
import main.api.response.ResultResponse;
import main.enums.Mode;
import main.enums.ModerationStatus;
import main.enums.UserStatus;
import main.model.Post;
import main.model.PostVote;
import main.model.User;
import main.repository.PostRepository;
import main.repository.VoteRepository;
import main.service.mapper.PostMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final AuthService authService;
    private final VoteRepository voteRepository;
    private final TagService tagService;

    private static final String DEFAULT_SORT_BY = "time";

    @Autowired
    public PostService(PostRepository postRepository, PostMapper postMapper, AuthService authService, VoteRepository voteRepository, TagService tagService) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
        this.authService = authService;
        this.voteRepository = voteRepository;
        this.tagService = tagService;
    }

    public PostViewResponse getPosts(int offset, int limit, Mode mode) {
        Page<Post> postPage;
        switch (mode) {
            case BEST:
                postPage = postRepository.findBestPosts(PageRequest.of(offset / limit, limit), new Date());
                break;
            case POPULAR:
                postPage = postRepository.findPopularPosts(PageRequest.of(offset / limit, limit), new Date());
                break;
            default:
                postPage = postRepository
                        .findPosts(getPageRequest(offset, limit, mode.getSortDirection(), mode.getSortBy()), new Date());
        }
        return getPostViewResponse(postPage);
    }

    public PostViewResponse getPostsByText(int offset, int limit, String query) {
        Page<Post> postPage = postRepository
                .findPostsWithText(getPageRequest(offset, limit, null, null), getLikeQuery(query), new Date());
        return getPostViewResponse(postPage);
    }

    public PostViewResponse getPostsByDate(int offset, int limit, String date) {
        Page<Post> postPage = postRepository
                .findPostsWithDate(getPageRequest(offset, limit, null, null), date, new Date());
        return getPostViewResponse(postPage);
    }

    public PostViewResponse getPostsByTag(int offset, int limit, String tag) {
        Page<Post> postPage = postRepository
                .findPostsWithTag(getPageRequest(offset, limit, null, null), tag, new Date());
        return getPostViewResponse(postPage);
    }

    public PostViewResponse searchPostToModerator(int offset, int limit, ModerationStatus moderationStatus) {
        Page<Post> postPage;
        if (ModerationStatus.NEW == moderationStatus) {
            postPage = postRepository.findPostsWithModerationStatus(
                    getPageRequest(offset, limit, null, null),
                    moderationStatus);
        } else {
            postPage = postRepository.findPostsToModeratorWithStatus(
                    getPageRequest(offset, limit, null, null),
                    moderationStatus,
                    authService.getCurrentUser().getId());
        }
        return getPostViewResponse(postPage);
    }

    public PostViewResponse getUserPost(int offset, int limit, UserStatus userStatus) {
        Page<Post> postPage = postRepository.findPostsToUser(
                getPageRequest(offset, limit, null, null),
                authService.getCurrentUser().getId(),
                userStatus.getModerationStatus(),
                userStatus.isActive());
        return getPostViewResponse(postPage);
    }

    public PostResponse getById(int id) {
        Post post = postRepository.findPostById(id);
        if (!post.getUser().getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName()) &&
                !authService.isModerator()) {
            post = postRepository.save(post.setViewCount(post.getViewCount() + 1));
        }
        return postMapper.toPostResponse(post);
    }

    public ResultResponse create(PostRequest postRequest) {
        Map<String, String> errors = checkPost(postRequest);
        if (errors.isEmpty()) {
            Post entity = postMapper.toModel(postRequest, tagService.getTags(postRequest.getTags()))
                    .setModerationStatus(ModerationStatus.NEW)
                    .setUser(authService.getCurrentUser());
            postRepository.save(entity);
            return getResult(true, null);
        } else {
            return getResult(false, errors);
        }
    }

    public ResultResponse edit(PostRequest postRequest, int id) {
        Map<String, String> errors = checkPost(postRequest);
        if (errors.isEmpty()) {
            Post entity = postMapper.toModel(postRequest, tagService.getTags(postRequest.getTags()))
                    .setModerationStatus(ModerationStatus.NEW)
                    .setUser(authService.getCurrentUser())
                    .setId(id);
            postRepository.save(entity);
            return getResult(true, null);
        } else {
            return getResult(false, errors);
        }
    }

    public boolean vote(int postId, boolean isLike) {
        User user = authService.getCurrentUser();
        Boolean value = voteRepository.findValue(user.getId(), postId);
        if (value == null) {
            voteRepository.save(new PostVote()
                    .setUser(user)
                    .setPost(postRepository.findPostById(postId))
                    .setTime(new Date())
                    .setValue(isLike));
            return true;
        } else if (value != isLike) {
            voteRepository.updateValue(isLike, user.getId(), postId);
            return true;
        } else {
            return false;
        }
    }

    private PostViewResponse getPostViewResponse(Page<Post> postPage) {
        return new PostViewResponse()
                .setPosts(postMapper.toViewList(postPage.toList()))
                .setCount(postPage.getTotalElements());
    }

    private ResultResponse getResult(boolean success, Map<String, String> errors) {
        return success ? new ResultResponse().setResult(true)
                : new ResultResponse().setResult(false).setErrors(errors);
    }

    private PageRequest getPageRequest(int offset, int limit, String direction, String sortBy) {
        if (direction == null) {
            direction = Sort.Direction.DESC.name();
        }
        if (sortBy == null) {
            sortBy = DEFAULT_SORT_BY;
        }
        return PageRequest.of(
                offset / limit, limit, Sort.Direction.fromString(direction), sortBy);
    }

    private Map<String, String> checkPost(PostRequest entity) {
        HashMap<String, String> errors = new HashMap<>(2);
        if (entity.getTitle().isEmpty()) {
            errors.put("title", "Заголовок не установлен");
        } else if (entity.getTitle().length() < 3) {
            errors.put("title", "Текст заголовка слишком короткий");
        }
        if (entity.getText().isEmpty()) {
            errors.put("text", "Текст публикации не установлен");
        } else if (entity.getText().length() < 50) {
            errors.put("text", "Текст публикации слишком короткий");
        }
        if (errors.isEmpty()) {
            Date date = new Date();
            if (entity.getTimestamp() == null && date.after(new Date(entity.getTimestamp() * 1000))) {
                entity.setTimestamp(date.getTime());
            }
        }
        return errors;
    }

    private String getLikeQuery(String query) {
        if (StringUtils.isBlank(query)) {
            query = "";
        }
        return "%" + query + "%";
    }
}
