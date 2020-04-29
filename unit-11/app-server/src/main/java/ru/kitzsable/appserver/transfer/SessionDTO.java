package ru.kitzsable.appserver.transfer;

import lombok.NoArgsConstructor;
import ru.kitzsable.appserver.model.Session;

import java.time.LocalDate;

/**
 * DTO Сессии
 */
@NoArgsConstructor
public class SessionDTO extends JournalLineDTO{
    public LocalDate insertDate;
    public double result;

    public SessionDTO(Session session) {
        this.id = session.getId().toString();
        this.name = session.getName();
        this.insertDate = session.getDate();
        this.result = session.getPercent();
    }
}
