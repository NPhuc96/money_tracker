package personal.phuc.expense.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import personal.phuc.expense.entity.user.AppUser;

import javax.persistence.*;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;


@Entity(name = "Group")
@Table(name = "groups")
@Data
public class Groups {

    @Id
    @SequenceGenerator(
            name = "groups_seq",
            sequenceName = "groups_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "groups_seq"
    )
    private Integer id;

    @Column(
            name = "name",
            columnDefinition = "TEXT",
            nullable = false,
            unique = true
    )
    private String name;

    @OneToMany(cascade = {REFRESH, MERGE}, mappedBy = "groups", fetch = LAZY)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Transaction> transactions;

    @ManyToOne(cascade = {MERGE, REFRESH}, fetch = LAZY)
    @JoinColumn(name = "app_user_id", referencedColumnName = "id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private AppUser appUser;
}
