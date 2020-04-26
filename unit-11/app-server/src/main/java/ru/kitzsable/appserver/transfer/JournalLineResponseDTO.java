package ru.kitzsable.appserver.transfer;

import java.util.List;

/**
 * DTO Ответа на запрос строк журнала
 */
public class JournalLineResponseDTO {
    public int total;
    public List<? extends JournalLineDTO> items;

    public JournalLineResponseDTO(int total, List<? extends JournalLineDTO> item) {
        this.total = total;
        this.items = item;
    }
}
