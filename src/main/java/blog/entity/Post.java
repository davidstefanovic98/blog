package blog.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Blog post
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "post")
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Post extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "post_id")
    private Integer id;
    @JoinColumn(name = "user_fk", referencedColumnName = "user_id")
    @ManyToOne
    private User user;
    @JoinColumn(name = "category_fk", referencedColumnName = "category_id")
    @ManyToOne
    private Category category;
    @Column(name = "title")
    private String title;
    @Column(name = "excerpt")
    private String excerpt;
    @Column(name = "body")
    private String body;
    @Column(name = "published")
    private Boolean published;
    @Column(name = "views")
    private Integer views;
    @Column(name = "slug")
    private String slug;
    @Column(name = "date_posted")
    private LocalDate datePosted;
    @ManyToMany
    @JoinTable(name = "post_tag", joinColumns = @JoinColumn(name = "post_fk"), inverseJoinColumns = @JoinColumn(name = "tag_fk"))
    private List<Tag> tags;


    public Post createPostForTesting(Integer postId, String title, Category category, String excerpt, String body, String slug) {
        Post post = new Post();
        post.setId(postId);
        post.setTitle(title);
        post.setExcerpt(excerpt);
        post.setCategory(category);
        post.setBody(body);
        post.setSlug(slug);
        return post;
    }
}
