package main.controller;

import main.api.request.PostRequest;
import main.api.response.PostResponse;
import main.api.response.PostViewResponse;
import main.api.response.ResultResponse;
import main.enums.Mode;
import main.enums.ModerationStatus;
import main.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class PostController {
    private static final String DEFAULT_SEARCH = "/api/post";
    private static final String SEARCH = "/api/post/search";
    private static final String BY_DATE = "/api/post/byDate";
    private static final String BY_TAG = "/api/post/byTag";
    private static final String MODERATION = "/api/post/moderation";
    private static final String MY = "/api/post/my";
    private static final String BY_ID = "/api/post/{id}";

    private PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping(DEFAULT_SEARCH)
    private ResponseEntity<PostViewResponse> defaultSearch(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "mode", required = false, defaultValue = "recent") String mode
    ) {
        return ResponseEntity.ok(postService.getPosts(offset, limit, Mode.getLookup().get(mode)));
    }

    @GetMapping(SEARCH)
    private ResponseEntity<PostViewResponse> search(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "query", required = false, defaultValue = "") String query
    ) {
        return ResponseEntity.ok(postService.getPostsByText(offset, limit, query));
    }

    @GetMapping(BY_DATE)
    private ResponseEntity<PostViewResponse> searchByDate(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "date") Date date
    ) {
        return ResponseEntity.ok(postService.getPostsByDate(offset, limit, date));
    }

    @GetMapping(BY_TAG)
    private ResponseEntity<PostViewResponse> searchByTag(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "tag") String tagName
    ) {
        return ResponseEntity.ok(postService.getPostsByTag(offset, limit, tagName));
    }

    @GetMapping(MODERATION)
    private ResponseEntity<PostViewResponse> searchByModerationStatus(
            @RequestParam(name = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(name = "limit", required = false, defaultValue = "10") int limit,
            @RequestParam(name = "status") String status
    ) {
        return ResponseEntity.ok(postService.searchByModerationStatus(offset, limit, ModerationStatus.valueOf(status)));
    }

    // без авторизации не знаю как реализовать
    @GetMapping(MY)
    private ResponseEntity<PostViewResponse> searchMy(
            @RequestParam(name = "offset") int offset,
            @RequestParam(name = "limit") int limit,
            @RequestParam(name = "status") String[] moderationStatus
    ) {
        return ResponseEntity.ok(postService.getCurrentUserPost(offset, limit, moderationStatus));
    }

    //без авторизации неполностью готов метод
    @GetMapping(BY_ID)
    private ResponseEntity<PostResponse> getById(@PathVariable int id) {
        return ResponseEntity.ok(postService.getById(id));
    }

    @PostMapping()
    private ResponseEntity<ResultResponse> create(PostRequest entity) {
        ResultResponse response = postService.create(entity);
        return ResponseEntity.ok(response);
    }

    @PutMapping(BY_ID)
    private ResponseEntity<ResultResponse> edit(@PathVariable int id, PostRequest postRequest) {
        ResultResponse response = postService.edit(postRequest, id);
        return ResponseEntity.ok(response);
    }
}
