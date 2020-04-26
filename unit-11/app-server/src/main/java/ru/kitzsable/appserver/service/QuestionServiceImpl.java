package ru.kitzsable.appserver.service;

import org.springframework.stereotype.Service;
import ru.kitzsable.appserver.model.Answer;
import ru.kitzsable.appserver.model.Question;
import ru.kitzsable.appserver.repository.AnswerRepository;
import ru.kitzsable.appserver.repository.QuestionRepository;
import ru.kitzsable.appserver.transfer.QuestionDTO;

import javax.transaction.Transactional;

@Service
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    /**
     * Метод обновления вопроса в БД
     * @param questionDTO DTO обновленного вопроса
     * @return Обновленный вопрос
     */
    @Override
    public QuestionDTO update(QuestionDTO questionDTO) {
        // Получение вопроса из БД по id из DTO
        Question question = questionRepository.findById(Long.parseLong(questionDTO.id))
                .orElseThrow(() ->
                        new RuntimeException("Попытка получить вопрос из БД с несуществующим id: " +
                                questionDTO.id));
        // Удаление ответов на полученный вопрос из БД
        answerRepository.deleteAllByQuestion(question);
        // Удаление вопроса
        questionRepository.deleteById(Long.parseLong(questionDTO.id));
        // Сохранение нового вопроса и возвращение DTO сохраненнго вопроса
        return save(questionDTO);
    }

    /**
     * Метод сохранения нового вопроса
     * @param questionDTO DTO нового вопроса
     * @return DTO сохраненного вопроса
     */
    @Override
    public QuestionDTO save(QuestionDTO questionDTO) {
        // Создание вопроса из DTO
        Question question = Question.builder()
                .name(questionDTO.name)
                .build();
        // Сохранение вопроса в БД
        questionRepository.save(question);
        // Создание ответов из DTO и их сохранение в БД
        questionDTO.answers.forEach(answerDTO ->
                answerRepository.save(Answer.builder()
                        .answerText(answerDTO.answerText)
                        .question(question)
                        .isCorrect(answerDTO.isCorrect)
                        .build()));
        // Возвращение DTO вопроса с сохраненными данными в БД
        return new QuestionDTO(question, answerRepository.findAllByQuestion(question));
    }
}