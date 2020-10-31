package main.service;

import main.api.response.CalendarResponse;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class CalendarService {
    private final PostRepository postRepository;

    @Autowired
    public CalendarService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public CalendarResponse getPosts(String requestYear) {
        int years;
        if (requestYear == null) {
            years = LocalDate.now().getYear();
        } else {
            years = Integer.valueOf(requestYear);
        }
        CalendarResponse calendarResponse = new CalendarResponse();
        postRepository.findYearSortedByCreatedPosts();
        postRepository.count();
        return null;
    }
}
