package blog.entity.domain;


import blog.entity.Post;
import blog.entity.Tag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@Builder
public class PostSummary {
    private Map<String, Long> summary = null;
    private Integer count = 0;
    @JsonIgnore
    private List<Post> posts = null;
    @JsonIgnore
    private List<Tag> tags = null;

    public PostSummary byCategory() {
        if (posts == null)
            throw new IllegalStateException("PostSummary posts is null");
        count = posts.size();
        summary = posts.stream()
                .collect(Collectors.groupingBy(p -> p.getCategory().getName(), Collectors.counting()));
        return this;
    }

    public PostSummary byTag() {
        if (posts == null)
            throw new IllegalStateException("PostSummary posts is null");
        if (tags == null)
            throw new IllegalStateException("PostSummary tags is null");
        count = posts.size();
        summary = new HashMap<>();
        tags.forEach(tag -> summary.put(tag.getName(), 0L));
        posts.forEach(post -> {
            if (post.getTags() != null) {
                post.getTags().forEach(tag -> summary.put(tag.getName(), summary.get(tag.getName()) + 1));
            }
        });
        return this;
    }

    public enum SummaryType {
        TAG, CATEGORY
    }
}