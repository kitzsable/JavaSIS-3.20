package ru.kitzsable.appserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.kitzsable.appserver.model.Journal;
import ru.kitzsable.appserver.service.JournalServiceImpl;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class AppServerApplication {

	private final JournalServiceImpl journalService;

	public AppServerApplication(JournalServiceImpl journalService) {
		this.journalService = journalService;
	}

	public static void main(String[] args) {
		SpringApplication.run(AppServerApplication.class, args);
	}

	/**
	 * Метод инициализации данных о журналах
	 */
	@PostConstruct
	private void initData() {
		journalService.save(Journal.builder()
				.id(JournalServiceImpl.QUESTIONS_JOURNAL_ID)
				.name("Вопросы")
				.defaultPageSize(15L)
				.build());
		journalService.save(Journal.builder()
				.id(JournalServiceImpl.SESSIONS_JOURNAL_ID)
				.name("Сессии")
				.defaultPageSize(10L)
				.build());
	}
}
