package main.api.request;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Accessors(chain = true)
public class ProfileRequest {
    private String name;
    private String email;
    private String password;
    private Boolean removePhoto;
    private MultipartFile photo;
}
