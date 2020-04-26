package ru.kitzsable.appserver.transfer;

import lombok.NoArgsConstructor;

/**
 * DTO Ответов на вопросы в сессии
 */
@NoArgsConstructor
public class SessionQuestionAnswerDTO {
    public String id;
    public Boolean isSelected;
}
