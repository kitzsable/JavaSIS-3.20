package ru.kitzsable.appserver.transfer;

import lombok.NoArgsConstructor;
import ru.kitzsable.appserver.model.Answer;

/**
 * DTO Ответа на вопрос
 */
@NoArgsConstructor
public class AnswerDTO {
    public String id;
    public String answerText;
    public Boolean isCorrect;

    public AnswerDTO(Answer answer) {
        id = answer.getId().toString();
        answerText = answer.getAnswerText();
        isCorrect = answer.getIsCorrect();
    }
}
