package ru.kitzsable.appserver.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.kitzsable.appserver.model.Journal;
import ru.kitzsable.appserver.model.JournalLine;
import ru.kitzsable.appserver.repository.AnswerRepository;
import ru.kitzsable.appserver.repository.JournalRepository;
import ru.kitzsable.appserver.repository.QuestionRepository;
import ru.kitzsable.appserver.repository.SessionRepository;
import ru.kitzsable.appserver.transfer.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Transactional
public class JournalServiceImpl implements JournalService{

    // Идентификаторы журналов
    public static final String QUESTIONS_JOURNAL_ID = "questions";
    public static final String SESSIONS_JOURNAL_ID = "sessions";

    private final JournalRepository journalRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final SessionRepository sessionRepository;

    public JournalServiceImpl(JournalRepository journalRepository, QuestionRepository questionRepository,
                              AnswerRepository answerRepository, SessionRepository sessionRepository) {
        this.journalRepository = journalRepository;
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.sessionRepository = sessionRepository;
    }

    /**
     * Метод сохранения экземпляра журнала
     * @param journal Журнал
     */
    @Override
    public void save(Journal journal) {
        journalRepository.save(journal);
    }

    /**
     * Метод получения журнала
     * @param id Идентификатор журнала
     * @return Журнал
     */
    @Override
    public JournalResponseDTO getJournal(String id) {
        return new JournalResponseDTO(journalRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Попытка получить журнал из БД с несуществующим id: " + id)));
    }

    /**
     * Метод создания ответа со строками журнала
     * @param id Идентификатор журнала
     * @param request Запрос
     * @return Ответ со строками
     */
    @Override
    public JournalLineResponseDTO getJournalLineResponse(String id, JournalLineRequestDTO request) {
        // Ответ со строками журнала и массив строк журнала
        JournalLineResponseDTO response;
        List<? extends JournalLineDTO> lines;
        // Получение строк нужного журнала, в зависимости от идентификатора,
        // а также инициализация ответа
        switch (id) {
            case QUESTIONS_JOURNAL_ID:
                lines = getJournalLines(request,
                        questionRepository::findByNameContainingIgnoreCase,
                        question ->
                                new QuestionDTO(question,
                                        answerRepository.findAllByQuestion(question)),
                        request.filters.isEmpty() || request.filters.get(0).value==null ?
                                questionDTO -> true :
                                questionDTO ->
                                        questionDTO.answers.size() ==
                                                Double.parseDouble(request.filters.get(0).value));
                response = new JournalLineResponseDTO( (int)questionRepository.count(), lines);
                break;
            case SESSIONS_JOURNAL_ID:
                lines = getJournalLines(request,
                            sessionRepository::findByNameContainingIgnoreCase,
                            SessionDTO::new,
                            sessionDTO -> true);
                response = new JournalLineResponseDTO( (int)sessionRepository.count(), lines);
                break;
            default:
                throw new RuntimeException("В url содержится неизвестный id журнала: " + id);
        }
        return response;
    }

    /**
     * Метод получения строк журнала
     * @param request Запрос
     * @param generator Функция получения записей журнала из БД
     * @param mapper Функция преобразования записи БД к DTO, соответствующей этому типу записей
     * @param filter Условие фильтрации
     * @param <T> Вид записей журналов в БД
     * @param <U> DTO, соответствующий виду записей
     * @return Список из DTO строк журнала
     */
    private <T extends JournalLine, U extends JournalLineDTO>
    List<? extends U> getJournalLines(JournalLineRequestDTO request,
                                      BiFunction<String, PageRequest, List<T>> generator,
                                      Function<T, U> mapper,
                                      Predicate<U> filter) {
        // Создание paginator по параметрам запроса
        // с прямой сортировкой по полю "name"
        PageRequest pageRequest = PageRequest.of(request.page-1, request.pageSize,
                Sort.Direction.ASC, "name");
        // Генерируется список элементов журнала из БД,
        // элементы преобразуются к соответствующим DTO,
        // проводится фильтрация по заданному предикату,
        // возвращается получившийся список из DTO строк журнала
        return generator.apply(request.search, pageRequest)
                .stream()
                .map(mapper)
                .filter(filter)
                .collect(Collectors.toList());
    }
}
