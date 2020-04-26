package ru.kitzsable.appserver.service;

import ru.kitzsable.appserver.transfer.QuestionDTO;

public interface QuestionService {
    QuestionDTO save(QuestionDTO questionDTO);
    QuestionDTO update(QuestionDTO questionDTO);
}
