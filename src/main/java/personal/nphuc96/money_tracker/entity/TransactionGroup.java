package personal.nphuc96.money_tracker.entity;

import lombok.Data;

import javax.persistence.*;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "EventGroup")
@Table(
        name = "event_group",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "event_group_name_unique",
                        columnNames = "name")
        }
)
@Data
public class EventGroup {

    @Id
    @SequenceGenerator(
            name = "event_group_sequence",
            sequenceName = "event_group_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "event_group_sequence"
    )
    private Integer id;

    @Column(
            name = "name",
            columnDefinition = "TEXT",
            nullable = false
    )
    private String name;
}
