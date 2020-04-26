package ru.kitzsable.appserver.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.kitzsable.appserver.model.Question;

import java.util.List;

@Repository
public interface QuestionRepository extends CrudRepository<Question, Long>,
        PagingAndSortingRepository<Question, Long> {
    List<Question> findByNameContainingIgnoreCase(String search, Pageable pageable);
    List<Question> findAll();
}
