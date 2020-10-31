package main.api.response;

import java.util.Date;
import java.util.Map;

public class CalendarResponse {
    private String[] years;
    private Map<Date, Integer> posts;

    public String[] getYears() {
        return years;
    }

    public CalendarResponse setYears(String[] years) {
        this.years = years;
        return this;
    }

    public Map<Date, Integer> getPosts() {
        return posts;
    }

    public CalendarResponse setPosts(Map<Date, Integer> posts) {
        this.posts = posts;
        return this;
    }
}
