package blog.entity;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

import static blog.entity.Role.ADMIN_ROLE;
import static blog.entity.domain.RecordStatus.*;
import static com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY;

@Data
@Entity
@Table(name = "user")
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class User extends Auditable implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "user_id")
    private Integer id;
    // TODO: maybe later.
//    @JoinColumn(name = "image_fk", referencedColumnName = "media_id")
//    @ManyToOne
//    private Media image;
    @Column(name = "username")
    private String username;
    @JsonProperty(access = WRITE_ONLY)
    @Column(name = "password")
    private String password;
    @Column(name = "email")
    private String email;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "about")
    private String about;
    @Column(name = "display_name")
    private String displayName;
    @ManyToMany(fetch = FetchType.EAGER)
    @JsonProperty(access = WRITE_ONLY)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_fk"), inverseJoinColumns = @JoinColumn(name = "role_fk"))
    private List<Role> roles;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private List<Contact> contacts;

    @JsonIgnore
    public boolean isAdmin() {
        return roles.stream().anyMatch(r -> r.equals(ADMIN_ROLE));
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return getRecordStatus() != EXPIRED;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return getRecordStatus() != LOCKED;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return getRecordStatus() != EXPIRED;
    }

    @JsonIgnore
    public boolean isCredentialsExpired() {
        return getRecordStatus() == EXPIRED;
    }

    @JsonIgnore
    public boolean isDisabled() {
        return getRecordStatus() == DISABLED;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return getRecordStatus() == ACTIVE;
    }

    @JsonIgnore
    public String getDefaultPassword() {
        return "blog-" + getUsername();
    }
}