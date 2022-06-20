package blog.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User permissions
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "role")
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Role extends Auditable implements GrantedAuthority {
    public static final Role ADMIN_ROLE = new Role(1, "ADMIN");
    public static final Role AUTHOR_ROLE = new Role(2, "AUTHOR");
    public static final Role USER_ROLE = new Role(3, "USER");
    public static final String PREFIX = "ROLE_";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "role_id")
    private Integer id;
    @Column(name = "name")
    private String name;
    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<User> users = new ArrayList<>();

    public Role(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    @JsonIgnore
    public String getAuthority() {
        return PREFIX + name;
    }
}
