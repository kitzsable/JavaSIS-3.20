package ru.kitzsable.appserver.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.kitzsable.appserver.model.SelectedAnswer;
import ru.kitzsable.appserver.model.Session;

import java.util.List;

@Repository
public interface SelectedAnswerRepository extends CrudRepository<SelectedAnswer, Long> {
    List<SelectedAnswer> findAllBySession(Session session);
}
