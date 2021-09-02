package personal.nphuc96.money_tracker.entity.app_user;

import lombok.*;
import personal.nphuc96.money_tracker.entity.Groups;
import personal.nphuc96.money_tracker.entity.Transaction;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

import static javax.persistence.CascadeType.*;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "AppUser")
@Table(name = "app_user")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppUser {

    @Id
    @SequenceGenerator(
            name = "app_user_sequence",
            sequenceName = "app_user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "app_user_sequence"

    )
    @Column(
            name = "id",
            updatable = false
    )
    private Integer id;

    @Column(
            name = "email",
            nullable = false
    )
    private String email;

    @Column(
            name = "h_password",
            nullable = false
    )
    private String password;

    @Column(
            name = "is_enabled",
            nullable = false
    )
    private Boolean isEnabled;

    @Column(
            name = "is_non_locked",
            nullable = false
    )
    private Boolean isNonLocked;

    @ManyToMany(cascade = {PERSIST, REFRESH, MERGE}, fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    @OneToMany(cascade = {ALL},
            fetch = FetchType.EAGER,
            mappedBy = "appUser",
            orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Transaction> transactions;

    @OneToMany(cascade = {ALL},
            fetch = FetchType.EAGER,
            mappedBy = "appUser",
            orphanRemoval = true)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Groups> groups;


}
