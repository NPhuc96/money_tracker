package personal.nphuc96.money_tracker.entity;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "TransactionGroup")
@Table(
        name = "transaction_group",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "transaction_group_name_unique",
                        columnNames = "name")
        }
)
@Data
public class TransactionGroup {

    @Id
    @SequenceGenerator(
            name = "transaction_group_sequence",
            sequenceName = "transaction_group_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "transaction_group_sequence"
    )
    private Integer id;

    @Column(
            name = "name",
            columnDefinition = "TEXT",
            nullable = false
    )
    private String name;
}
