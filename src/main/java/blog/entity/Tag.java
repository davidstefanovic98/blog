package blog.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

/**
 * Blog post tag
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "tag")
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Tag extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "tag_id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @ManyToMany
    @JsonIgnore
    @JoinTable(name = "post_tag", joinColumns = @JoinColumn(name = "tag_fk"), inverseJoinColumns = @JoinColumn(name = "post_fk"))
    private List<Post> posts;

}