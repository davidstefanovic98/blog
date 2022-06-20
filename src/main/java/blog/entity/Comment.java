package blog.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "comment")
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Comment extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "comment_id")
    private Integer id;
    @JoinColumn(name = "user_fk", referencedColumnName = "user_id")
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "parent")
    private List<Comment> comments;
    @ManyToOne
    @JoinColumn(name = "comment_fk", referencedColumnName = "comment_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Comment parent;
    @JoinColumn(name = "post_fk", referencedColumnName = "post_id")
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Post post;
    @Column(name = "body")
    private String body;
}