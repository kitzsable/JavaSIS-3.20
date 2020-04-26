package ru.kitzsable.appserver.transfer;

import lombok.EqualsAndHashCode;
import ru.kitzsable.appserver.model.Journal;

/**
 * DTO Ответа на запрос метаданных журнала
 */
@EqualsAndHashCode
public class JournalResponseDTO {
    public String id;
    public String name;
    public Long defaultPageSize;

    public JournalResponseDTO(Journal journal) {
        this.id = journal.getId();
        this.name = journal.getName();
        this.defaultPageSize = journal.getDefaultPageSize();
    }
}
