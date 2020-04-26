package ru.kitzsable.appserver.transfer;

import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO Запроса строк журнала
 */
@NoArgsConstructor
public class JournalLineRequestDTO {
    public String journalId;
    public String search;
    public List<JournalFilterDTO> filters;
    public int page;
    public int pageSize;
}
