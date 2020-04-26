package ru.kitzsable.appserver.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Session extends JournalLine {
    @Column
    private String name;

    @Column
    private Date date;

    @Column
    private Integer percent;
}
