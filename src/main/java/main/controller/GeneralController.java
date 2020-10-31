package main.controller;

import main.api.request.CommentRequest;
import main.api.request.ModerationRequest;
import main.api.response.CalendarResponse;
import main.api.response.InitResponse;
import main.api.response.ResultResponse;
import main.api.response.SettingsResponse;
import main.api.response.model.Tag;
import main.service.CalendarService;
import main.service.CommentService;
import main.service.ModerationService;
import main.service.TagService;
import main.service.UploadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GeneralController {
    private static final String INIT = "/api/init";
    private static final String SETTINGS = "/api/settings";
    private static final String TAG = "/api/tag";
    private static final String MODERATION = "/api/moderation";
    private static final String CALENDAR = "/api/calendar";
    private static final String IMAGE = "/api/image";
    private static final String COMMENT = "/api/comment";

    private final InitResponse initResponse;
    private final TagService tagService;
    private final ModerationService moderationService;
    private final CalendarService calendatService;
    private final UploadService uploadService;
    private final CommentService commentService;

    public GeneralController(InitResponse initResponse, TagService tagService, ModerationService moderationService, CalendarService calendatService, UploadService uploadService, CommentService commentService) {
        this.initResponse = initResponse;
        this.tagService = tagService;
        this.moderationService = moderationService;
        this.calendatService = calendatService;
        this.uploadService = uploadService;
        this.commentService = commentService;
    }

    @GetMapping(INIT)
    private InitResponse init() {
        return initResponse;
    }

    @GetMapping(SETTINGS)
    private ResponseEntity<SettingsResponse> settings() {
        return ResponseEntity.ok(new SettingsResponse());
    }

    @GetMapping(TAG)
    private ResponseEntity<List<Tag>> getTag(
            @RequestParam(name = "query", required = false, defaultValue = "") String query
    ) {
        return ResponseEntity.ok(tagService.getTags(query));
    }

    @PostMapping(MODERATION)
    private ResponseEntity<Boolean> checkPost(ModerationRequest entity) {
        return ResponseEntity.ok(moderationService.approveComment(entity));
    }

    @GetMapping(CALENDAR)
    private ResponseEntity<CalendarResponse> getPosts(
            @RequestParam(name = "year", required = false) String year
    ) {
        return ResponseEntity.ok(calendatService.getPosts(year));
    }

    //не знаю как реализовать, что использовать?
    @PostMapping(IMAGE)
    private ResponseEntity<ResultResponse> upload() {
        ResultResponse response = uploadService.upload();
        if (response.isResult()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping(COMMENT)
    private ResponseEntity<ResultResponse> addComment(CommentRequest commentRequest) {
        ResultResponse response = commentService.addComment(commentRequest);
        return ResponseEntity.ok(response);
    }
}
