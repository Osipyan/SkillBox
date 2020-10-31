package main.service.mapper;

import main.api.request.PostRequest;
import main.api.response.PostResponse;
import main.api.response.model.Comment;
import main.api.response.model.PostView;
import main.api.response.model.UserName;
import main.api.response.model.UserView;
import main.model.Post;
import main.model.PostComment;
import main.model.PostVote;
import main.model.Tag;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PostMapper {
    public PostResponse toPostResponse(Post post) {
        return new PostResponse()
                .setId(post.getId())
                .setTime(post.getTime().getTime() / 1000)
                .setUser(
                        new UserName().setId(post.getUser().getId()).setName(post.getUser().getName())
                )
                .setTitle(post.getTitle())
                .setLikeCount(
                        (int) post.getPostVotes().stream().filter(PostVote::isValue).count()
                )
                .setDislikeCount(
                        (int) post.getPostVotes().stream().filter(vote -> !vote.isValue()).count()
                )
                .setCommentCount(post.getComments().size())
                .setViewCount(post.getViewCount())
                .setText(post.getText())
                .setComments(toWarComment(post.getComments()))
                .setTags(post.getTags().stream()
                        .map(tag -> tag.getName())
                        .collect(Collectors.toSet())
                        .toArray(String[]::new)
                );
    }

    public Post toModel(PostRequest post, Set<Tag> tags) {
        return new Post()
                .setText(post.getText())
                .setTime(new Date(post.getTimestamp() * 1000))
                .setTitle(post.getTitle())
                .setActive(post.isActive())
                .setTags(tags);
    }

    public List<PostView> toViewList(List<Post> entityList) {
        return entityList.stream()
                .map(model -> toView(model))
                .collect(Collectors.toList());
    }

    private List<Comment> toWarComment(List<PostComment> comments) {
        return comments.stream()
                .map(model -> new Comment()
                        .setId(model.getId())
                        .setTimestamp(model.getTime().getTime() / 1000)
                        .setText(model.getText())
                        .setUser(new UserView().setPhoto(model.getUser().getPhoto()))
                ).collect(Collectors.toList());
    }

    private PostView toView(Post post) {
        PostView postView = new PostView()
                .setId(post.getId())
                .setTime(post.getTime().getTime() / 1000)
                .setUser(
                        new UserName().setId(post.getUser().getId()).setName(post.getUser().getName())
                )
                .setTitle(post.getTitle())
                .setLikeCount(
                        (int) post.getPostVotes().stream().filter(PostVote::isValue).count()
                )
                .setDislikeCount(
                        (int) post.getPostVotes().stream().filter(vote -> !vote.isValue()).count()
                )
                .setCommentCount(post.getComments().isEmpty() ? 0 : post.getComments().size())
                .setViewCount(post.getViewCount());
        String text = Jsoup.parse(post.getText()).text();
        postView.setAnnounce(
                text.length() > 147 ? text.substring(0, 146).concat("...") : text
        );
        return postView;
    }
}
