package main.controller;

import main.api.request.LikeRequest;
import main.api.request.PostRequest;
import main.api.response.PostResponse;
import main.api.response.PostViewResponse;
import main.api.response.ResultResponse;
import main.enums.Mode;
import main.enums.ModerationStatus;
import main.enums.UserStatus;
import main.service.PostService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PostController {
    private static final String DEFAULT_SEARCH = "/post";
    private static final String SEARCH = "/post/search";
    private static final String BY_DATE = "/post/byDate";
    private static final String BY_TAG = "/post/byTag";
    private static final String MODERATION = "/post/moderation";
    private static final String MY = "/post/my";
    private static final String BY_ID = "/post/{id}";
    private static final String LIKE = "/post/like";
    private static final String DISLIKE = "/post/dislike";

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(DEFAULT_SEARCH)
    public PostViewResponse defaultSearch(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "mode", required = false, defaultValue = "recent") String mode
    ) {
        return postService.getPosts(offset, limit, Mode.getLookup().get(mode));
    }

    @GetMapping(SEARCH)
    public PostViewResponse search(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "query", required = false, defaultValue = "") String query
    ) {
        return postService.getPostsByText(offset, limit, query);
    }

    @GetMapping(BY_DATE)
    public PostViewResponse searchByDate(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "date") String date
    ) {
        return postService.getPostsByDate(offset, limit, date);
    }

    @GetMapping(BY_TAG)
    public PostViewResponse searchByTag(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "tag") String tagName
    ) {
        return postService.getPostsByTag(offset, limit, tagName);
    }

    @GetMapping(MODERATION)
    @PreAuthorize("hasAuthority('user:moderate')")
    public PostViewResponse searchPostToModerator(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "status") String status
    ) {
        return postService.searchPostToModerator(offset, limit, ModerationStatus.valueOf(status.toUpperCase()));
    }

    @GetMapping(MY)
    @PreAuthorize("hasAuthority('user:write')")
    public PostViewResponse searchMy(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "status") String status
    ) {
        return postService.getUserPost(offset, limit, UserStatus.valueOf(status.toUpperCase()));
    }

    @GetMapping(BY_ID)
    public PostResponse getById(@PathVariable int id) {
        return postService.getById(id);
    }

    @PostMapping(DEFAULT_SEARCH)
    @PreAuthorize("hasAuthority('user:write')")
    public ResultResponse create(@RequestBody PostRequest entity) {
        return postService.create(entity);
    }

    @PutMapping(BY_ID)
    @PreAuthorize("hasAuthority('user:write')")
    public ResultResponse edit(@PathVariable int id, @RequestBody PostRequest postRequest) {
        return postService.edit(postRequest, id);
    }

    @PostMapping(LIKE)
    @PreAuthorize("hasAuthority('user:write')")
    public Boolean like(@RequestBody LikeRequest likeRequest) {
        return postService.vote(likeRequest.getPostId(), true);
    }

    @PostMapping(DISLIKE)
    @PreAuthorize("hasAuthority('user:write')")
    public boolean dislike(@RequestBody LikeRequest likeRequest) {
        return postService.vote(likeRequest.getPostId(), false);
    }
}
