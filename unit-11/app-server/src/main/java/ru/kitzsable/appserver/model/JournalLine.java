package ru.kitzsable.appserver.model;

import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@Data
@MappedSuperclass
public class JournalLine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
}
