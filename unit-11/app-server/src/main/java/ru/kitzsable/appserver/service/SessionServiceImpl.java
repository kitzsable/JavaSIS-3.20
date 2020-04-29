package ru.kitzsable.appserver.service;

import org.springframework.stereotype.Service;
import ru.kitzsable.appserver.model.Question;
import ru.kitzsable.appserver.model.SelectedAnswer;
import ru.kitzsable.appserver.model.Session;
import ru.kitzsable.appserver.repository.AnswerRepository;
import ru.kitzsable.appserver.repository.QuestionRepository;
import ru.kitzsable.appserver.repository.SelectedAnswerRepository;
import ru.kitzsable.appserver.repository.SessionRepository;
import ru.kitzsable.appserver.transfer.QuestionDTO;
import ru.kitzsable.appserver.transfer.SessionCreateDTO;
import ru.kitzsable.appserver.transfer.SessionQuestionAnswerDTO;

import javax.transaction.Transactional;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class SessionServiceImpl implements SessionService {

    // Количество вопросов в сессии
    private static final int COUNT_OF_QUESTIONS_IN_SESSION = 5;
    // Минимум баллов за ответы на один вопрос
    private static final double MIN_POINT_PER_QUESTIONS = 0d;

    private final SessionRepository sessionRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final SelectedAnswerRepository selectedAnswerRepository;

    public SessionServiceImpl(SessionRepository sessionRepository, QuestionRepository questionRepository,
                              AnswerRepository answerRepository, SelectedAnswerRepository selectedAnswerRepository) {
        this.sessionRepository = sessionRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.selectedAnswerRepository = selectedAnswerRepository;
    }

    /**
     * Метод создания сессии
     * @param createDTO DTO создаваемой сессии
     * @return Колличество набранных процентов за сессию
     */
    @Override
    public String createSession(SessionCreateDTO createDTO) {
        // Подсчитываются баллы набранные за каждый вопрос,
        // затем суммируются в общее колличество баллов,
        // а после считается процент набранных балло от возможного максимума
        // и округляется до целого значения
        double percent = createDTO.questionsList
                .stream()
                .map(question -> calculatePointsPerQuestion(question.answersList))
                .reduce(0d, Double::sum)
                / createDTO.questionsList.size() * 100;
        // Создание сессии из DTO и её сохранение в БД
        Session session = Session.builder()
                .name(createDTO.name)
                .date(LocalDate.now())
                .percent(percent)
                .build();
        sessionRepository.save(session);
        // Сохранение всех выбранных пользователем ответов
        saveSelectedAnswers(session, createDTO);
        // Округление до 2х цифр после запятой
        return new DecimalFormat("#.0#").format(percent);
    }

    /**
     * Метод генерации вопросов для сессии
     * @return Список, содержащий DTO вопросов
     */
    @Override
    public List<QuestionDTO> generateQuestions() {
        List<QuestionDTO> result;
        // Вытягиваются все вопросы из БД
        List<Question> questions = questionRepository.findAll();
        // Если количество вопросов не больше 5, то
        // возвращается список вопросов преобразованных в DTO.
        // Иначе генерируются номера,
        // вопросы под которыми преобразуются в DTO.
        if (questions.size() <= COUNT_OF_QUESTIONS_IN_SESSION) {
            result = questions.stream()
                    .map(question ->
                            new QuestionDTO(question,
                                    answerRepository.findAllByQuestion(question)))
                    .collect(Collectors.toList());
        } else {
            result = generateIndexes(questions.size())
                    .stream()
                    .map(index ->
                            new QuestionDTO(questions.get(index),
                                    answerRepository.findAllByQuestion(questions.get(index))))
                    .collect(Collectors.toList());
        }
        // Скрываются ответы на вопросы
        result.stream()
                .flatMap(questionDTO ->
                        questionDTO.answers.stream())
                .forEach(answerDTO ->
                        answerDTO.isCorrect=null);
        return result;
    }

    /**
     * Метод, сохраняющий выбранные пользователем ответы
     * @param session Сессия пользователя
     * @param createDTO Запрос на создание сессии с выбранными ответами
     */
    private void saveSelectedAnswers(Session session, SessionCreateDTO createDTO) {
        createDTO.questionsList
                .stream()
                .flatMap(answeredQuestionDTO ->
                        answeredQuestionDTO.answersList.stream())
                .filter(answerDTO -> answerDTO.isSelected)
                .map(answerDTO ->
                        answerRepository.findById(Long.parseLong(answerDTO.id))
                                .orElseThrow(() ->
                                        new RuntimeException("Попытка получить вопрос из БД с несуществующим id: " +
                                                answerDTO.id)))
                .forEach(answer ->
                        selectedAnswerRepository.save(SelectedAnswer.builder()
                                .session(session)
                                .answer(answer)
                                .build()));
    }

    /**
     * Метод вычисляющий количество баллов за ответы на один вопрос
     * @param answersDTO Список DTO ответов
     * @return Количество баллов
     */
    private double calculatePointsPerQuestion(List<SessionQuestionAnswerDTO> answersDTO) {
        // Количество ответов на вопрос,
        // количество правильных ответов на вопрос,
        // количество выбранных правильных ответов,
        // количество выбранных неправильных ответов.
        double count = answersDTO.size(),
                right=0d,
                selectedRight=0d,
                selectedWrong=0d;
        // Для каждого ответа
        for (SessionQuestionAnswerDTO answerDTO: answersDTO) {
            // Проверяется правильный ли это ответ
            boolean isRight = answerRepository
                    .findById(Long.parseLong(answerDTO.id))
                    .orElseThrow(() ->
                            new RuntimeException("Попытка получить ответ из БД с несуществующим id: " +
                                    answerDTO.id))
                    .getIsCorrect();
            // Проверяется выбран ли этот ответ
            boolean isSelected = answerDTO.isSelected;
            // Если ответ правильный и выбран, то
            // количество правильных и выбранных правильных увеличивается.
            // Если ответ только правильный увеличивается количество правильных, а
            // если только выбранный то увеличивается количество выбранных неправильных
            if (isSelected && isRight) {
                right++;
                selectedRight++;
            } else if (isRight) {
                right++;
            } else if (isSelected) {
                selectedWrong++;
            }
        }
        // Расчитывается и возвращается результат по заданной формуле.
        // В случае равенства количества верных и всех ответов,
        // формула изменяется, чтобы избежать деления на 0.
        if (count==right) {
            return selectedRight/right;
        } else {
            return Math.max( MIN_POINT_PER_QUESTIONS, selectedRight/right - selectedWrong/(count-right) );
        }
    }

    /**
     * Метод генерирующий номера вопросов для сессий
     * @param max Верхняя граница номера (не включается)
     * @return Список номеров
     */
    private List<Integer> generateIndexes(int max) {
        ArrayList<Integer> indexes = new ArrayList<>();
        // Пока не сгенерировано необходимое кол-во вопросов
        for (int i = 0; i < COUNT_OF_QUESTIONS_IN_SESSION; i++) {
            Random random = new Random();
            // Генерируется случайное число [0;max)
            int index = random.nextInt(max);
            // Если это число ещё не было сгенерировано,
            // оно добавляется в список.
            // Иначе число ггенерируется заново
            if (!indexes.contains(index)) {
                indexes.add(index);
            } else {
                i--;
            }
        }
        return indexes;
    }
}
