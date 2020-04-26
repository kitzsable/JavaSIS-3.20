package ru.kitzsable.appserver.transfer;

import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO Запроса на создание сессии
 */
@NoArgsConstructor
public class SessionCreateDTO {
    public String name;
    public List<AnsweredQuestionDTO> questionsList;
}
