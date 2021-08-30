package personal.nphuc96.money_tracker.entity.app_user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

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

    @ManyToMany(cascade = {PERSIST, REFRESH, MERGE})
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "app_user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;
}
