package ru.kitzsable.appserver.service;

import ru.kitzsable.appserver.transfer.QuestionDTO;
import ru.kitzsable.appserver.transfer.SessionCreateDTO;

import java.util.List;

public interface SessionService {
    String createSession(SessionCreateDTO createDTO);
    List<QuestionDTO> generateQuestions();
}
