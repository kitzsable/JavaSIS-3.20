package ru.kitzsable.appserver.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.kitzsable.appserver.model.Session;

import java.util.List;

@Repository
public interface SessionRepository extends CrudRepository<Session, Long>,
        PagingAndSortingRepository<Session, Long>,
        JpaRepository<Session, Long> {
    List<Session> findByNameContainingIgnoreCase(String search, Pageable pageable);
}
