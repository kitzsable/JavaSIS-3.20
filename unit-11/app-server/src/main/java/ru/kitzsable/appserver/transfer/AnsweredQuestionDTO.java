package ru.kitzsable.appserver.transfer;

import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO Вопрос, на которые были получены ответы в сессии
 */
@NoArgsConstructor
public class AnsweredQuestionDTO {
    public String id;
    public List<SessionQuestionAnswerDTO> answersList;
}
