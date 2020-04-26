package ru.kitzsable.appserver.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.kitzsable.appserver.model.Journal;

@Repository
public interface JournalRepository extends CrudRepository<Journal, String> {
}
