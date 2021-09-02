package personal.nphuc96.money_tracker.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import personal.nphuc96.money_tracker.entity.app_user.AppUser;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.GenerationType.SEQUENCE;


/*@Table(
        name = "groups",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "group_name_unique",
                        columnNames = "name")
        }
)*/
@Entity(name = "Group")
@Table(name = "groups")
@Data
public class Groups {

    @Id
    @SequenceGenerator(
            name = "groups_sequence",
            sequenceName = "groups_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "groups_sequence"
    )
    private Integer id;

    @Column(
            name = "name",
            columnDefinition = "TEXT",
            nullable = false,
            unique = true
    )
    private String name;

    @OneToMany(cascade = {REFRESH, MERGE}, mappedBy = "groups")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Transaction> transactionSet;

    @ManyToOne(cascade = {MERGE, REFRESH})
    @JoinColumn(name = "app_user_id", referencedColumnName = "id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private AppUser appUser;
}
