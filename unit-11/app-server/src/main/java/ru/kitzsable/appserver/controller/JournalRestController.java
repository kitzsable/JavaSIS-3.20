package ru.kitzsable.appserver.controller;

import org.springframework.web.bind.annotation.*;
import ru.kitzsable.appserver.service.JournalServiceImpl;
import ru.kitzsable.appserver.transfer.JournalLineRequestDTO;
import ru.kitzsable.appserver.transfer.JournalLineResponseDTO;
import ru.kitzsable.appserver.transfer.JournalResponseDTO;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/journal")
public class JournalRestController {

    private final JournalServiceImpl journalService;

    public JournalRestController(JournalServiceImpl journalService) {
        this.journalService = journalService;
    }

    @GetMapping("{id}")
    public JournalResponseDTO getJournal(@PathVariable String id) {
        return journalService.getJournal(id);
    }

    @PutMapping("{id}/rows")
    public JournalLineResponseDTO getJournalLines(@PathVariable String id,
                                                  @RequestBody JournalLineRequestDTO request) {
        return journalService.getJournalLineResponse(id, request);
    }
}
