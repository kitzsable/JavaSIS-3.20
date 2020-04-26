package ru.kitzsable.appserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.kitzsable.appserver.model.Answer;
import ru.kitzsable.appserver.model.Question;

import java.util.List;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Long>,
        JpaRepository<Answer, Long> {
    List<Answer> findAllByQuestion(Question question);
    void deleteAllByQuestion(Question question);
}
