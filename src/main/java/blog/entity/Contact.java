package blog.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.*;

/**
 * User contact information entry
 */
@Data
@Entity
@NoArgsConstructor
@Table(name = "contact")
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Contact extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "contact_id")
    private Integer id;
    @Column(name = "contact_type")
    private String contactType;
    @JsonIgnore
    @JoinColumn(name = "user_fk", referencedColumnName = "user_id")
    @ManyToOne
    private User user;
    @Column(name = "value")
    private String value;
}