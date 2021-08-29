package personal.nphuc96.money_tracker.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.GenerationType.SEQUENCE;


@Entity(name = "Transaction")
@Data
@Table(name = "transaction")
public class Transaction {

    @Id
    @SequenceGenerator(
            name = "transaction_sequence",
            sequenceName = "transaction_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "transaction_sequence"

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
            name = "time",
            columnDefinition = "DATE",
            nullable = false

    )
    private LocalDate time;

    @Column(
            name = "money",
            nullable = false
    )
    private BigDecimal money;

    @OneToOne(
            cascade = {MERGE, REFRESH}
    )
    @JoinColumn(name = "transaction_group_id", referencedColumnName = "id")
    private TransactionGroup transactionGroup;
}
