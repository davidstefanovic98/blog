package blog.entity;

import javax.persistence.*;
import lombok.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "notification")
@EqualsAndHashCode(callSuper = false, onlyExplicitlyIncluded = true)
public class Notification extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name = "notification_id")
    private Integer id;
    @Column(name = "title")
    private String title;
    @Column(name = "body")
    private String body;
    @Column(name = "seen")
    private boolean seen;
    @Column(name = "`read`")
    private boolean read;
    @JoinColumn(name = "user_fk", referencedColumnName = "user_id")
    @ManyToOne
    private User user;
    @Column(name = "type")
    private String type;
    @Column(name = "action_url")
    private String actionUrl;

    public Notification(Notification other) {
        this.id = other.id;
        this.title = other.title;
        this.body = other.body;
        this.seen = other.seen;
        this.read = other.read;
        this.user = other.user;
        this.type = other.type;
        this.actionUrl = other.actionUrl;
    }
}
