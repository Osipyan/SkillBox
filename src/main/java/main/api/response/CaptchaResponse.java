package main.api.response;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CaptchaResponse {
    private String secret;
    private String image;
}
