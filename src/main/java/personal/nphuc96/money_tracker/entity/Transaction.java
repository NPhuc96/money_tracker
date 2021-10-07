package personal.nphuc96.money_tracker.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import personal.nphuc96.money_tracker.entity.app_user.AppUser;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.SEQUENCE;


@Entity(name = "Transaction")
@Data
@Table(name = "transaction")
@NoArgsConstructor
public class Transaction {

    @Id
    @SequenceGenerator(
            name = "transaction_seq",
            sequenceName = "transaction_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "transaction_seq"

    )
    @Column(
            name = "id",
            updatable = false

    )
    private Integer id;
    @Column(
            name = "note",
            columnDefinition = "TEXT"
    )
    private String note;
    @Column(
            name = "on_date",
            columnDefinition = "DATE",
            nullable = false

    )
    private LocalDate onDate;
    @Column(
            name = "amount",
            nullable = false
    )
    private BigDecimal amount;
    @ManyToOne(cascade = {MERGE, REFRESH}, fetch = LAZY)
    @JoinColumn(name = "groups_id", referencedColumnName = "id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Groups groups;
    @ManyToOne(cascade = {MERGE, REFRESH}, fetch = LAZY)
    @JoinColumn(name = "app_user_id", referencedColumnName = "id", nullable = false)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private AppUser appUser;
    public Transaction(Integer id, BigDecimal amount) {
        this.id = id;
        this.amount = amount;
    }

    public Transaction(BigDecimal amount) {
        this.amount = amount;
    }
}
