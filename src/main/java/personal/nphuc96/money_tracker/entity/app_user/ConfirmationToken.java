package personal.nphuc96.money_tracker.entity.app_user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.SEQUENCE;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "ConfirmationToken")
@Table(name = "confirmation_token")
public class ConfirmationToken {

    @Id
    @SequenceGenerator(
            name = "confirmation_token_seq",
            sequenceName = "confirmation_token_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "confirmation_token_seq"

    )
    @Column(
            name = "id",
            updatable = false
    )
    private Integer id;

    @Column(
            name = "token",
            updatable = false,
            nullable = false
    )
    private String token;


    @Column(name = "creation_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime creationTime;


    @Column(name = "expiration_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime expirationTime;


    @Column(name = "confirmation_time", columnDefinition = "TIMESTAMP")
    private LocalDateTime confirmationTime;

    @Column(name = "is_confirmed")
    private Boolean isConfirmed;

    @Column(name = "app_user_id")
    private Integer userId;
}
