package ru.kitzsable.appserver.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.LocalDate;

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
    private LocalDate date;

    @Column
    private Double percent;
}
