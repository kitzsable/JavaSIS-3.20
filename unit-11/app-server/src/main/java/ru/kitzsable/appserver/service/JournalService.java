package ru.kitzsable.appserver.service;

import ru.kitzsable.appserver.model.Journal;
import ru.kitzsable.appserver.transfer.JournalLineRequestDTO;
import ru.kitzsable.appserver.transfer.JournalLineResponseDTO;
import ru.kitzsable.appserver.transfer.JournalResponseDTO;

public interface JournalService {
    void save(Journal journal);
    JournalResponseDTO getJournal(String id);
    JournalLineResponseDTO getJournalLineResponse(String id, JournalLineRequestDTO request);
}
