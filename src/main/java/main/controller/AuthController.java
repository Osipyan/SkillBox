package main.controller;

import main.api.request.LoginRequest;
import main.api.request.PasswordRequest;
import main.api.request.ProfileRequest;
import main.api.request.RegisterRequest;
import main.api.request.RestoreRequest;
import main.api.response.CaptchaResponse;
import main.api.response.LoginResponse;
import main.api.response.ResultResponse;
import main.api.response.StatisticsResponse;
import main.service.AuthService;
import main.service.BadRequestException;
import main.service.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@RestController
@RequestMapping("/api")
public class AuthController {
    private static final String CHECK = "/auth/check";
    private static final String LOGIN = "/auth/login";
    private static final String CAPTCHA = "/auth/captcha";
    private static final String REGISTER = "/auth/register";
    private static final String RESTORE = "/auth/restore";
    private static final String PASSWORD = "/auth/password";
    private static final String UPDATE = "/profile/my";
    private static final String USER_STATISTICS = "/statistics/my";
    private static final String ALL_STATISTICS = "/statistics/all";
    private static final String LOGOUT = "/auth/logout";

    private final AuthenticationManager authenticationManager;
    private final AuthService authService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    @RequestMapping(LOGIN)
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        Authentication auth;
        try {
            auth = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            return new LoginResponse().setResult(false);
        }
        SecurityContextHolder.getContext().setAuthentication(auth);
        return authService.getLoginResponse(loginRequest.getEmail());
    }

    @GetMapping(CHECK)
    public LoginResponse check(Principal principal) {
        return principal == null ?
                new LoginResponse() : authService.getLoginResponse(principal.getName());
    }

    @GetMapping(CAPTCHA)
    public CaptchaResponse getCaptcha() throws IOException {
        return authService.getCaptcha();
    }

    @PostMapping(REGISTER)
    public ResultResponse register(@RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping(path = UPDATE, consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('user:write')")
    public boolean updateProfileWithFoto(@ModelAttribute ProfileRequest request) throws IOException {
        return authService.updateProfile(request);
    }

    @PostMapping(path = UPDATE)
    @PreAuthorize("hasAuthority('user:write')")
    public boolean updateProfile(@RequestBody ProfileRequest request) throws IOException {
        return authService.updateProfile(request);
    }

    @PostMapping(RESTORE)
    public boolean updateProfile(@RequestBody RestoreRequest request) {
        return authService.restore(request.getEmail());
    }

    @PostMapping(PASSWORD)
    public boolean changePassword(@RequestBody PasswordRequest request) {
        return authService.changePassword(request);
    }

    @GetMapping(USER_STATISTICS)
    @PreAuthorize("hasAuthority('user:write')")
    public StatisticsResponse getUserStatistics() {
        return authService.getUserStatistics();
    }

    @GetMapping(ALL_STATISTICS)
    public StatisticsResponse getAllStatistics() {
        return authService.getAllStatistics();
    }

    @GetMapping(LOGOUT)
    public boolean logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return true;
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ResultResponse> handleBadRequestExceptionError(BadRequestException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ResultResponse()
                        .setResult(false)
                        .setErrors(ex.getErrors()));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity handleUnauthorizedExceptionError(UnauthorizedException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(null);
    }
}
