package main.service;

import main.api.response.TagResponse;
import main.api.response.model.Tag;
import main.repository.PostRepository;
import main.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TagService {
    private TagRepository tagRepository;
    private PostRepository postRepository;

    @Autowired
    public TagService(TagRepository tagRepository, PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    public TagResponse getTags(String query) {
        Date date = new Date();
        List<main.model.Tag> tagList = tagRepository.findAllTagByNameIsLike("%" + query + "%", date);
        for (main.model.Tag tag : tagList) {
            tag.setPosts(tag.getPosts().stream()
                    .filter(p -> p.isActive() && "ACCEPTED".equals(p.getModerationStatus().name()) && date.after(p.getTime()))
                    .collect(Collectors.toSet()));
        }
        int countPostsByMostPopularTag = tagRepository.findCountPostsByMostPopularTag(new Date());
        int postCount = postRepository.findPosts(new Date()).size();
        List<Tag> tagsResponse = tagList.stream()
                .map(t -> new Tag()
                        .setName(t.getName())
                        .setWeight(getTagWeight(countPostsByMostPopularTag, postCount, t.getPosts().size())))
                .collect(Collectors.toList());
        return new TagResponse()
                .setTags(tagsResponse);
    }

    public Set<main.model.Tag> getTags(String[] tags) {
        return Arrays.stream(tags)
                .map(this::getOrCreateTag)
                .collect(Collectors.toSet());
    }

    private main.model.Tag getOrCreateTag(String tagName) {
        main.model.Tag tag = tagRepository.findTagByName(tagName);
        return tag == null ? tagRepository.save(new main.model.Tag().setName(tagName)) : tag;
    }

    private double getTagWeight(double postCountInMostPopularTag, double postCount, double postCountInTag) {
        //ненормированный веc
        double dWeight = postCountInTag / postCount;
        //коэффициент нормализации
        double k = 1 / (postCountInMostPopularTag / postCount);
        return dWeight * k;
    }
}
