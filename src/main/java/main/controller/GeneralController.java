package main.controller;

import main.api.request.CommentRequest;
import main.api.request.ModerationRequest;
import main.api.response.CalendarResponse;
import main.api.response.InitResponse;
import main.api.response.ResultResponse;
import main.api.response.SettingsResponse;
import main.api.response.TagResponse;
import main.service.BadRequestException;
import main.service.CalendarService;
import main.service.CommentService;
import main.service.ExtensionException;
import main.service.ModerationService;
import main.service.SettingsService;
import main.service.StorageService;
import main.service.TagService;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@ControllerAdvice
@RequestMapping("/api")
public class GeneralController {
    private static final String INIT = "/init";
    private static final String SETTINGS = "/settings";
    private static final String TAG = "/tag";
    private static final String MODERATION = "/moderation";
    private static final String CALENDAR = "/calendar";
    private static final String IMAGE = "/image";
    private static final String COMMENT = "/comment";

    private final InitResponse initResponse;
    private final TagService tagService;
    private final ModerationService moderationService;
    private final CalendarService calendarService;
    private final CommentService commentService;
    private final StorageService storageService;
    private final SettingsService settingsService;

    public GeneralController(InitResponse initResponse, TagService tagService, ModerationService moderationService, CalendarService calendarService, StorageService storageService, CommentService commentService, SettingsService settingsService) {
        this.initResponse = initResponse;
        this.tagService = tagService;
        this.moderationService = moderationService;
        this.calendarService = calendarService;
        this.storageService = storageService;
        this.commentService = commentService;
        this.settingsService = settingsService;
    }

    @GetMapping(INIT)
    public InitResponse init() {
        return initResponse;
    }

    @GetMapping(SETTINGS)
    public SettingsResponse settings() {
        return settingsService.getSettings();
    }

    @PutMapping(SETTINGS)
    public void saveSettings(@RequestBody SettingsResponse request) {
        settingsService.saveSettings(request);
    }

    @GetMapping(TAG)
    public TagResponse getTag(@RequestParam(name = "query", required = false, defaultValue = "") String query) {
        return tagService.getTags(query);
    }

    @PostMapping(MODERATION)
    @PreAuthorize("hasAuthority('user:moderate')")
    public boolean moderatePost(@RequestBody ModerationRequest entity) {
        return moderationService.moderatePost(entity);
    }

    @GetMapping(CALENDAR)
    public CalendarResponse getPosts(@RequestParam(name = "year", required = false) String year) {
        return calendarService.getPosts(year);
    }

    @PostMapping(IMAGE)
    @PreAuthorize("hasAuthority('user:write')")
    public String upload(@RequestParam() MultipartFile image) throws IOException {
        return storageService.upload(image);
    }

    @PostMapping(COMMENT)
    @PreAuthorize("hasAuthority('user:write')")
    public ResultResponse addComment(@RequestBody CommentRequest commentRequest) {
        return commentService.addComment(commentRequest);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResultResponse> handleMaxUploadSizeExceededExceptionError(MaxUploadSizeExceededException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ResultResponse()
                        .setResult(false)
                        .setErrors(Map.of("image",
                                "Размер файла превышает допустимый размер " + ((SizeLimitExceededException) ex.getCause().getCause()).getPermittedSize())));
    }

    @ExceptionHandler(ExtensionException.class)
    public ResponseEntity<ResultResponse> handleExtensionExceptionError(ExtensionException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ResultResponse()
                        .setResult(false)
                        .setErrors(Map.of("image",
                                "Недопустимый формат файла. " + ex.getMessage())));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResultResponse> handleBadRequestExceptionError(BadRequestException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ResultResponse()
                        .setResult(false)
                        .setErrors(ex.getErrors()));
    }
}
