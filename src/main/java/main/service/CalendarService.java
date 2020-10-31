package main.service;

import main.api.response.CalendarResponse;
import main.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CalendarService {
    private final PostRepository postRepository;

    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    public CalendarService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public CalendarResponse getPosts(String requestYear) {
        int year;
        if (requestYear == null) {
            year = LocalDate.now().getYear();
        } else {
            year = Integer.valueOf(requestYear);
        }
        Map<String, Integer> createdPostsByDay = new HashMap<>();
        for (List<Object> list : postRepository.findCreatedPostByDayOfYears(year, new Date())) {
            String date = dateFormat.format(list.get(0));
            int count = ((BigInteger) list.get(1)).intValue();
            createdPostsByDay.put(date, count);
        }
        return new CalendarResponse()
                .setYears(postRepository.findYearSortedByCreatedPosts())
                .setPosts(createdPostsByDay);
    }
}
