package personal.phuc.expense.entity.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "PasswordResetCode")
@Table(name = "password_reset_token")
public class PasswordResetCode {
    @Id
    @SequenceGenerator(
            name = "password_reset_seq",
            sequenceName = "password_reset_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "password_reset_seq"
    )
    @Column(
            name = "id",
            updatable = false
    )
    private Integer id;

    @Column(
            name = "code",
            updatable = false,
            nullable = false
    )
    private String code;

    @Column(name = "is_confirmed")
    private Boolean isConfirmed;

    @Column(name = "email")
    private String email;
}
