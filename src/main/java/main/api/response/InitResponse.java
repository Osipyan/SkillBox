package main.api.response;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class InitResponse {
    @Value("${blog.title}")
    private String title;
    @Value("${blog.subtitle}")
    private String subtitle;
    @Value("${blog.phone}")
    private String phone;
    @Value("${blog.email}")
    private String email;
    @Value("${blog.copyright}")
    private String copyright;
    @Value("${blog.copyrightFrom}")
    private String copyrightFrom;

    public String getTitle() {
        return title;
    }

    public InitResponse setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public InitResponse setSubtitle(String subtitle) {
        this.subtitle = subtitle;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public InitResponse setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public InitResponse setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getCopyright() {
        return copyright;
    }

    public InitResponse setCopyright(String copyright) {
        this.copyright = copyright;
        return this;
    }

    public String getCopyrightFrom() {
        return copyrightFrom;
    }

    public InitResponse setCopyrightFrom(String copyrightFrom) {
        this.copyrightFrom = copyrightFrom;
        return this;
    }
}