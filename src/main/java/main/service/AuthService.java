package main.service;

import com.github.cage.Cage;
import com.github.cage.GCage;
import main.api.request.PasswordRequest;
import main.api.request.ProfileRequest;
import main.api.request.RegisterRequest;
import main.api.response.CaptchaResponse;
import main.api.response.LoginResponse;
import main.api.response.ResultResponse;
import main.api.response.StatisticsResponse;
import main.api.response.UserLoginResponse;
import main.enums.ModerationStatus;
import main.enums.Role;
import main.model.CaptchaCode;
import main.model.Post;
import main.model.User;
import main.repository.CaptchaRepository;
import main.repository.PostRepository;
import main.repository.SettingsRepository;
import main.repository.UserRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {
    @Value("${minutesBeforeDeleteCaptcha}")
    private int minutesBeforeDeleteCaptcha;

    private final static String CAPTCHA_ERROR = "Код с картинки введён неверно";
    private final static String PASSWORD_ERROR = "Пароль короче 6-ти символов";
    private final static String EMAIL_EXIST_ERROR = "Этот e-mail уже зарегистрирован";
    private final static String CAPTCHA_DEPRECATED_ERROR = "Ссылка для восстановления пароля устарела. <a href= \"/auth/restore\">Запросить ссылку снова</a>";
    private final static String STATISTICS_IS_PUBLIC = "STATISTICS_IS_PUBLIC";
    private final static String MAIL_ADDRESS = "testSendSpringMail@yandex.ru";
    private final static String MAIL_SUBJECT = "Восстановление пароля DevPub";
    private final static String MAIL_TEXT = "Ссылка на восстановление пароля:\n/login/change-password/";

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CaptchaRepository captchaRepository;
    private final PasswordEncoder passwordEncoder;
    private final StorageService storageService;
    private final SettingsRepository settingsRepository;
    private final JavaMailSender mailSender;

    @Autowired
    public AuthService(UserRepository userRepository, PostRepository postRepository, CaptchaRepository captchaRepository, PasswordEncoder passwordEncoder, StorageService storageService, SettingsRepository settingsRepository, JavaMailSender mailSender) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.captchaRepository = captchaRepository;
        this.passwordEncoder = passwordEncoder;
        this.storageService = storageService;
        this.settingsRepository = settingsRepository;
        this.mailSender = mailSender;
    }

    public LoginResponse getLoginResponse(String email) {
        User user = getUser(email);
        if (user == null) {
            new UsernameNotFoundException("user " + email + " not found");
        }
        return new LoginResponse()
                .setResult(true)
                .setUserLoginResponse(
                        new UserLoginResponse()
                                .setId(user.getId())
                                .setEmail(user.getEmail())
                                .setModeration(user.isModerator())
                                .setName(user.getName())
                                .setPhoto(user.getPhoto())
                                .setSettings(user.isModerator())
                                .setModerationCount(
                                        user.isModerator() ? (int) postRepository.findPostsWithModerationStatus(null, ModerationStatus.NEW).getTotalElements() : 0
                                ));
    }

    public User getUser(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public User getCurrentUser() {
        return userRepository
                .findByEmail(SecurityContextHolder.getContext().getAuthentication().getName())
                .orElse(null);
    }

    public boolean isModerator() {
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getAuthorities()
                .containsAll(Role.MODERATION.getAuthorities());
    }

    public CaptchaResponse getCaptcha() throws IOException {
        Cage cage = new GCage();
        OutputStream os = new FileOutputStream("captcha.jpg", false);
        try {
            String secret = UUID.randomUUID().toString();
            String code = cage.getTokenGenerator().next();
            byte[] fileContent = cage.draw(code);

            Date currentDate = new Date();
            captchaRepository.save(new CaptchaCode()
                    .setCode(code)
                    .setSecretCode(secret)
                    .setTime(currentDate));
            captchaRepository.deleteAllOldCaptcha(DateUtils.addMinutes(currentDate, -minutesBeforeDeleteCaptcha));

            return new CaptchaResponse()
                    .setImage("data:image/png;base64, " + Base64.getEncoder().encodeToString(fileContent))
                    .setSecret(secret);
        } finally {
            os.close();
        }
    }

    public ResultResponse register(RegisterRequest registerRequest) {
        Map<String, String> errors = checkRegisterRequest(registerRequest);
        if (errors.isEmpty()) {
            userRepository.save(new User()
                    .setEmail(registerRequest.getEmail())
                    .setName(registerRequest.getName())
                    .setReqTime(new Date())
                    .setPassword(passwordEncoder.encode(registerRequest.getPassword())));
            return new ResultResponse().setResult(true);
        } else {
            return new ResultResponse()
                    .setResult(false)
                    .setErrors(errors);
        }
    }

    public boolean updateProfile(ProfileRequest request) throws IOException {
        checkProfileData(request);
        userRepository.save(mergeData(request));
        return true;
    }

    public boolean restore(String email) {
        User user = getUser(email);
        if (user != null) {
            String hash = RandomStringUtils.random(45, true, true);
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(MAIL_ADDRESS);
            message.setTo(email);
            message.setSubject(MAIL_SUBJECT);
            message.setText(MAIL_TEXT + hash);
            mailSender.send(message);
            userRepository.saveHash(hash, email);
            return true;
        } else return false;
    }

    public boolean changePassword(PasswordRequest request) {
        User user = checkPasswordRequest(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
        return true;
    }

    public StatisticsResponse getUserStatistics() {
        return getStatistics(
                postRepository.findPostsToUserWithDefaultSort(getCurrentUser().getId(), new Date())
        );
    }

    public StatisticsResponse getAllStatistics() {
        if ("YES".equals(settingsRepository.findValueByCode(STATISTICS_IS_PUBLIC)) || isModerator()) {
            return getStatistics(
                    postRepository.findPostsWithDefaultSort(new Date())
            );
        } else {
            throw new UnauthorizedException();
        }
    }

    private StatisticsResponse getStatistics(List<Post> posts) {
        return new StatisticsResponse()
                .setPostsCount(posts.size())
                .setLikesCount(posts.stream()
                        .flatMap(post -> post.getPostVotes().stream())
                        .filter(vote -> vote.isValue()).count())
                .setDislikesCount(posts.stream()
                        .flatMap(post -> post.getPostVotes().stream())
                        .filter(vote -> !vote.isValue()).count())
                .setViewsCount(posts.stream().mapToLong(post -> post.getViewCount()).sum())
                .setFirstPublication(posts.get(0).getTime().getTime() / 1000);
    }

    private User mergeData(ProfileRequest request) throws IOException {
        User user = getCurrentUser();
        if (request.getName() != null) user.setName(request.getName());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPassword() != null) user.setPassword(request.getPassword());
        if (request.getRemovePhoto() != null && request.getRemovePhoto()) user.setPhoto(null);
        if (request.getPhoto() != null && !request.getPhoto().isEmpty()) {
            String imagePath = storageService.uploadProfilePhoto(request.getPhoto());
            user.setPhoto(imagePath);
        }
        return user;
    }

    private void checkProfileData(ProfileRequest request) {
        HashMap<String, String> errors = new HashMap<>(2);
        if (request.getPassword() != null) {
            if (request.getPassword().length() < 6) {
                errors.put("password", PASSWORD_ERROR);
            }
        }
        if (request.getEmail() != null && !request.getEmail().equals(SecurityContextHolder.getContext().getAuthentication().getName())) {
            if (userRepository.existsUserByEmail(request.getEmail())) {
                errors.put("email", EMAIL_EXIST_ERROR);
            }
        }
        if (!errors.isEmpty()) {
            throw new BadRequestException(errors);
        }
    }

    private Map<String, String> checkRegisterRequest(RegisterRequest registerRequest) {
        Map<String, String> errors = new HashMap<>(4);
        if (!registerRequest.getCaptcha().equals(captchaRepository.findCodeBySecretCode(registerRequest.getCaptchaSecret()))) {
            errors.put("captcha", CAPTCHA_ERROR);
        }
        if (userRepository.existsUserByEmail(registerRequest.getEmail())) {
            errors.put("email", EMAIL_EXIST_ERROR);
        }
        if (registerRequest.getPassword().length() < 6) {
            errors.put("password", PASSWORD_ERROR);
        }
        return errors;
    }

    private User checkPasswordRequest(PasswordRequest request) {
        Map<String, String> errors = new HashMap<>(4);
        String captchaCode = captchaRepository.findCodeBySecretCode(request.getCaptchaSecret());
        if (!request.getCaptcha().equals(captchaCode)) {
            errors.put("captcha", CAPTCHA_ERROR);
        }
        if (request.getPassword().length() < 6) {
            errors.put("password", PASSWORD_ERROR);
        }
        User user = userRepository.findByCode(request.getCode()).get();
        if (user == null) {
            errors.put("code", CAPTCHA_DEPRECATED_ERROR);
        }
        if (!errors.isEmpty()) {
            throw new BadRequestException(errors);
        }
        return user;
    }
}