package ru.kitzsable.appserver.transfer;

import lombok.NoArgsConstructor;
import ru.kitzsable.appserver.model.Answer;
import ru.kitzsable.appserver.model.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO Вопроса
 */
@NoArgsConstructor
public class QuestionDTO extends JournalLineDTO {
    public List<AnswerDTO> answers;

    public QuestionDTO(Question question, List<Answer> answers) {
        this.id = question.getId().toString();
        this.name = question.getName();
        this.answers = new ArrayList<>();
        answers.forEach(answer ->
                this.answers.add(
                        new AnswerDTO(answer)));
    }
}
