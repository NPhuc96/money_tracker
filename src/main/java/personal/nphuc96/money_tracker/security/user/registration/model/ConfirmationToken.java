package personal.nphuc96.money_tracker.security.user.registration.model;

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
@Entity(name = "confirmation_token")
public class ConfirmationToken {

    @Id
    @SequenceGenerator(
            name = "confirmation_token_sequence",
            sequenceName = "confirmation_token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "confirmation_token_sequence"

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

    /* @ManyToOne
     @JoinColumn(name = "app_user_id",referencedColumnName = "id")
     @EqualsAndHashCode.Exclude
     @ToString.Exclude*/
    @Column(name = "app_user_id")
    private Integer userId;
}
