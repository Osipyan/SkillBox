package main.service;

import main.api.request.PostRequest;
import main.api.response.PostResponse;
import main.api.response.PostViewResponse;
import main.api.response.ResultResponse;
import main.enums.Mode;
import main.enums.ModerationStatus;
import main.mapper.PostMapper;
import main.model.Post;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;

    private static final String DEFAULT_SORT_BY = "date";

    @Autowired
    public PostService(PostRepository postRepository, PostMapper postMapper) {
        this.postRepository = postRepository;
        this.postMapper = postMapper;
    }

    public PostViewResponse getPosts(int offset, int limit, Mode mode) {
        Page<Post> postPage = postRepository.findAllActivePosts(getPageRequest(offset, limit, mode.getSortDirection(), mode.getSortBy()));
        return new PostViewResponse()
                .setPosts(postMapper.toViewList(postPage.getContent()))
                .setCount(postPage.getTotalElements());
    }

    public PostViewResponse getPostsByText(int offset, int limit, String query) {
        Page<Post> postPage = postRepository.findAllActivePostsWithText(getPageRequest(offset, limit, null, null), query);
        return new PostViewResponse()
                .setPosts(postMapper.toViewList(postPage.toList()))
                .setCount(postPage.getSize());
    }

    public PostViewResponse getPostsByDate(int offset, int limit, Date query) {
        Page<Post> postPage = postRepository.findAllActivePostsWithDate(getPageRequest(offset, limit, null, null), query);
        return new PostViewResponse()
                .setPosts(postMapper.toViewList(postPage.toList()))
                .setCount(postPage.getSize());
    }

    public PostViewResponse getPostsByTag(int offset, int limit, String query) {
        Page<Post> postPage = postRepository.findAllActivePostsWithTag(getPageRequest(offset, limit, null, null), query);
        return new PostViewResponse()
                .setPosts(postMapper.toViewList(postPage.toList()))
                .setCount(postPage.getSize());
    }

    public PostViewResponse searchByModerationStatus(int offset, int limit, ModerationStatus moderationStatus) {
        Page<Post> postPage = postRepository.findAllActivePostsWithModerationStatus(getPageRequest(offset, limit, null, null), moderationStatus.name());
        return new PostViewResponse()
                .setPosts(postMapper.toViewList(postPage.toList()))
                .setCount(postPage.getSize());
    }

    public PostViewResponse getCurrentUserPost(int offset, int limit, String[] moderationStatus) {
        return null;
    }

    public PostResponse getById(int id) {
        Post post = postRepository.findPostById(id);
        boolean isModerator = false;
        boolean isOwner = false;
        if (isModerator || isOwner) {
            return postMapper.toPostResponse(post);
        } else if (post.getActive() && post.getModerationStatus().equals(ModerationStatus.ACCEPTED)) {
            post.setView_count(post.getView_count() + 1);
            //save post
            return postMapper.toPostResponse(post);
        } else {
            return null;
        }
    }

    public ResultResponse create(PostRequest postRequest) {
        Map<String, String> errors = checkPost(postRequest);
        if (errors.isEmpty()) {
            Post entity = postMapper.toModel(postRequest);
            entity.setModerationStatus(ModerationStatus.NEW);
            postRepository.save(entity);
            return new ResultResponse()
                    .setResult(true);
        } else {
            return new ResultResponse()
                    .setResult(false)
                    .setErrors(errors);
        }
    }

    public ResultResponse upload() {
        return null;
    }

    public ResultResponse edit(PostRequest postRequest, int id) {
        Map<String, String> errors = checkPost(postRequest);
        if (errors.isEmpty()) {
            Post entity = postMapper.toModel(postRequest);
            entity.setModerationStatus(ModerationStatus.NEW);
            entity.setId(id);
            postRepository.save(entity);
            return new ResultResponse()
                    .setResult(true);
        } else {
            return new ResultResponse()
                    .setResult(false)
                    .setErrors(errors);
        }
    }

    private PageRequest getPageRequest(int offset, int limit, String direction, String sortBy) {
        if (direction == null) {
            direction = Sort.Direction.DESC.name();
        }
        if (sortBy == null) {
            sortBy = DEFAULT_SORT_BY;
        }
        return PageRequest.of(
                offset, limit, Sort.Direction.fromString(direction), sortBy);
    }

    private Map<String, String> checkPost(PostRequest entity) {
        HashMap<String, String> errors = new HashMap<>(2);
        if (entity.getTitle().isEmpty()) {
            errors.put("title", "Заголовок не установлен");
        } else if (entity.getTitle().length() < 3) {
            errors.put("title", "Текст заголовка слишком короткий");
        }
        if (entity.getText().isEmpty()) {
            errors.put("title", "Текст публикации не установлен");
        } else if (entity.getTitle().length() < 50) {
            errors.put("title", "Текст публикации слишком короткий");
        }
        if (errors.isEmpty()) {
            Date date = new Date();
            if (date.after(entity.getTimestamp())) {
                entity.setTimestamp(date);
            }
        }
        return errors;
    }
}
