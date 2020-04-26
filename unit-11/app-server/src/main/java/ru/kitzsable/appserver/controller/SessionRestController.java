package ru.kitzsable.appserver.controller;

import org.springframework.web.bind.annotation.*;
import ru.kitzsable.appserver.service.SessionService;
import ru.kitzsable.appserver.transfer.QuestionDTO;
import ru.kitzsable.appserver.transfer.SessionCreateDTO;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/session")
public class SessionRestController {

    private final SessionService sessionService;

    public SessionRestController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("questions-new")
    public List<QuestionDTO> generateQuestions(){
        return sessionService.generateQuestions();
    }

    @PostMapping
    public String createSession(@RequestBody SessionCreateDTO sessionCreateDTO) {
        return sessionService.createSession(sessionCreateDTO);
    }
}
