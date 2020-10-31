package main.service;

import main.api.response.model.Tag;
import main.repository.PostRepository;
import main.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagService {
    private TagRepository tagRepository;
    private PostRepository postRepository;

    @Autowired
    public TagService(TagRepository tagRepository, PostRepository postRepository) {
        this.tagRepository = tagRepository;
        this.postRepository = postRepository;
    }

    public List<Tag> getTags(String query) {
        List<main.model.Tag> tagList = tagRepository.findAllTagByNameIsLike("%" + query + "%");
        int mostPopularTag = tagRepository.findCountPostsByMostPopularTag();
        int postCount = postRepository.findAll().size();
        List<Tag> tagsResponse = new ArrayList<>();
        for (main.model.Tag tag : tagList) {
            tagsResponse.add(new Tag()
                    .setName(tag.getName())
                    .setWeight(getTagWeight(mostPopularTag, postCount, tag.getPosts().size())));
        }
        return tagsResponse;
    }

    private double getTagWeight(double mostPopularTag, double postCount, double postCountInTag) {
        //коэффициент нормализации
        double k = 1 / mostPopularTag / postCount;
        return postCountInTag / postCount * k;
    }
}
