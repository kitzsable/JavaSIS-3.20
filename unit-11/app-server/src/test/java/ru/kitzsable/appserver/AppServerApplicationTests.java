package ru.kitzsable.appserver;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.junit4.SpringRunner;
import ru.kitzsable.appserver.model.*;
import ru.kitzsable.appserver.repository.*;
import ru.kitzsable.appserver.service.*;
import ru.kitzsable.appserver.transfer.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppServerApplicationTests {

	@Autowired
	private JournalService journalService;
	@Autowired
	private QuestionService questionService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private AnswerRepository answerRepository;
	@Autowired
	private JournalRepository journalRepository;
	@Autowired
	private SelectedAnswerRepository selectedAnswerRepository;
	@Autowired
	private SessionRepository sessionRepository;

	@Before
	public void initTestData() {
		Question question1 = Question.builder()
				.name("13*11")
				.build();
		questionRepository.save(question1);
		Answer answer1_1 = Answer.builder()
				.answerText("137")
				.question(question1)
				.isCorrect(false)
				.build();
		Answer answer1_2 = Answer.builder()
				.answerText("143")
				.question(question1)
				.isCorrect(true)
				.build();
		Answer answer1_3 = Answer.builder()
				.answerText("211")
				.question(question1)
				.isCorrect(false)
				.build();
		answerRepository.saveAll(Arrays.asList(answer1_1,answer1_2,answer1_3));

		Question question2 = Question.builder()
				.name("Первый президент РФ")
				.build();
		questionRepository.save(question2);
		Answer answer2_1 = Answer.builder()
				.answerText("Ельцин")
				.question(question2)
				.isCorrect(true)
				.build();
		Answer answer2_2 = Answer.builder()
				.answerText("Горбачёв")
				.question(question2)
				.isCorrect(false)
				.build();
		Answer answer2_3 = Answer.builder()
				.answerText("Ленин")
				.question(question2)
				.isCorrect(false)
				.build();
		Answer answer2_4 = Answer.builder()
				.answerText("Медведев")
				.question(question2)
				.isCorrect(false)
				.build();
		answerRepository.saveAll(Arrays.asList(answer2_1,answer2_2,answer2_3, answer2_4));

		Question question3 = Question.builder()
				.name("Самая высокая гора в мире")
				.build();
		questionRepository.save(question3);
		Answer answer3_1 = Answer.builder()
				.answerText("Эверест")
				.question(question3)
				.isCorrect(true)
				.build();
		Answer answer3_2 = Answer.builder()
				.answerText("Эльбрус")
				.question(question3)
				.isCorrect(false)
				.build();
		Answer answer3_3 = Answer.builder()
				.answerText("Джомолунгма")
				.question(question3)
				.isCorrect(true)
				.build();
		Answer answer3_4 = Answer.builder()
				.answerText("Сагарматха")
				.question(question3)
				.isCorrect(true)
				.build();
		answerRepository.saveAll(Arrays.asList(answer3_1,answer3_2,answer3_3, answer3_4));

		Question question4 = Question.builder()
				.name("Первый император России")
				.build();
		questionRepository.save(question4);
		Answer answer4_1 = Answer.builder()
				.answerText("Пётр Первый")
				.question(question4)
				.isCorrect(true)
				.build();
		answerRepository.saveAll(Collections.singletonList(answer4_1));

		Question question5 = Question.builder()
				.name("Какое дерево является симолом Канады")
				.build();
		questionRepository.save(question5);
		Answer answer5_1 = Answer.builder()
				.answerText("Клён")
				.question(question5)
				.isCorrect(true)
				.build();
		answerRepository.saveAll(Collections.singletonList(answer5_1));

		Question question6 = Question.builder()
				.name("Первый человек в космосе")
				.build();
		questionRepository.save(question6);
		Answer answer6_1 = Answer.builder()
				.answerText("Гагарин")
				.question(question6)
				.isCorrect(true)
				.build();
		answerRepository.saveAll(Collections.singletonList(answer6_1));

		Session session1 = Session.builder()
				.name("Мирослав Петров")
				.date(LocalDate.now())
				.percent(50d)
				.build();
		sessionRepository.save(session1);
		SelectedAnswer selectedAnswer1_1 = SelectedAnswer.builder()
				.session(session1)
				.answer(answer1_1)
				.build();
		SelectedAnswer selectedAnswer1_2 = SelectedAnswer.builder()
				.session(session1)
				.answer(answer1_2)
				.build();
		selectedAnswerRepository.saveAll(Arrays.asList(selectedAnswer1_1, selectedAnswer1_2));

		Session session2 = Session.builder()
				.name("Евгений Мирошников")
				.date(LocalDate.now())
				.percent(63d)
				.build();
		sessionRepository.save(session2);
		SelectedAnswer selectedAnswer2_1 = SelectedAnswer.builder()
				.session(session2)
				.answer(answer1_2)
				.build();
		SelectedAnswer selectedAnswer2_2 = SelectedAnswer.builder()
				.session(session2)
				.answer(answer1_3)
				.build();
		SelectedAnswer selectedAnswer2_3 = SelectedAnswer.builder()
				.session(session2)
				.answer(answer2_1)
				.build();
		SelectedAnswer selectedAnswer2_4 = SelectedAnswer.builder()
				.session(session2)
				.answer(answer2_4)
				.build();
		selectedAnswerRepository.saveAll(Arrays.asList(selectedAnswer2_1, selectedAnswer2_2,
				selectedAnswer2_3, selectedAnswer2_4));
	}

	@After
	public void cleanDB() {
		journalRepository.deleteAll();
		selectedAnswerRepository.deleteAll();
		sessionRepository.deleteAll();
		answerRepository.deleteAll();
		questionRepository.deleteAll();
	}

	@Test
	public void testSaveAndGetJournal() {
		Journal journal = Journal.builder()
				.id("testJournal")
				.name("Тест")
				.defaultPageSize(5L)
				.build();
		journalService.save(journal);
		assertEquals(journalService.getJournal(journal.getId()).id, journal.getId());
	}

	@Test
	public void testSavingQuestion() {
		QuestionDTO questionDTO = new QuestionDTO();
		questionDTO.name = "3+3";
		questionDTO.answers = new ArrayList<>();

		AnswerDTO answer1 = new AnswerDTO();
		answer1.answerText = "5";
		answer1.isCorrect = false;

		AnswerDTO answer2 = new AnswerDTO();
		answer2.answerText = "6";
		answer2.isCorrect = true;

		questionDTO.answers.add(answer1);
		questionDTO.answers.add(answer2);

		questionService.save(questionDTO);

		assertTrue(questionRepository.findAll()
				.stream()
				.map(Question::getName)
				.collect(Collectors.toList())
				.contains(questionDTO.name));
		assertTrue(answerRepository.findAll()
				.stream()
				.map(Answer::getAnswerText)
				.collect(Collectors.toList())
				.containsAll(Arrays.asList(answer1.answerText, answer2.answerText)));
	}

	@Test
	public void testResponseAfterSavingQuestion() {
		QuestionDTO questionDTO = new QuestionDTO();
		questionDTO.name = "2+2";
		questionDTO.answers = new ArrayList<>();

		AnswerDTO answer1 = new AnswerDTO();
		answer1.answerText = "3";
		answer1.isCorrect = false;

		AnswerDTO answer2 = new AnswerDTO();
		answer2.answerText = "4";
		answer2.isCorrect = true;

		questionDTO.answers.add(answer1);
		questionDTO.answers.add(answer2);

		QuestionDTO responseQuestionDTO = questionService.save(questionDTO);
		assertEquals(questionDTO.name, responseQuestionDTO.name);
		assertTrue(responseQuestionDTO.answers.stream()
				.map(answerDTO -> answerDTO.answerText)
				.collect(Collectors.toList())
				.containsAll(Arrays.asList(answer1.answerText, answer2.answerText)));
	}

	@Test
	public void testUpdateQuestion() {
		Question oldQuestion = questionRepository.findAll().get(4);
		List<Answer> oldAnswers = answerRepository.findAllByQuestion(oldQuestion);

		QuestionDTO questionDTO = new QuestionDTO();
		questionDTO.id = String.valueOf(oldQuestion.getId());
		questionDTO.name = "17*11";
		questionDTO.answers = new ArrayList<>();

		AnswerDTO answer1 = new AnswerDTO();
		answer1.answerText = "189";
		answer1.isCorrect = false;

		AnswerDTO answer2 = new AnswerDTO();
		answer2.answerText = "187";
		answer2.isCorrect = true;

		questionDTO.answers.add(answer1);
		questionDTO.answers.add(answer2);

		questionService.update(questionDTO);

		Question question = questionRepository.findById(Long.parseLong(questionDTO.id))
				.orElseThrow(() -> new RuntimeException("Не найден вопрос с id: " + questionDTO.id));
		assertTrue(answerRepository.findAllByQuestion(question)
				.stream()
				.map(Answer::getAnswerText)
				.collect(Collectors.toList())
				.containsAll(Arrays.asList(answer1.answerText, answer2.answerText)));
		assertTrue(questionRepository.findAll()
				.stream()
				.map(Question::getName)
				.collect(Collectors.toList())
				.contains(questionDTO.name));
	}

	@Test
	public void testGetLinesForQuestionJournal() {
		JournalLineRequestDTO request = new JournalLineRequestDTO();
		request.search = "";
		request.filters = new ArrayList<>();
		request.page = 1;
		request.pageSize = 15;

		JournalLineResponseDTO response = journalService
				.getJournalLineResponse(JournalServiceImpl.QUESTIONS_JOURNAL_ID, request);
		assertTrue(response.items.size() == questionRepository.count() &&
				response.total == response.items.size());
	}

	@Test
	public void testGetLinesForSessionJournal() {
		JournalLineRequestDTO request = new JournalLineRequestDTO();
		request.search = "";
		request.filters = new ArrayList<>();
		request.page = 1;
		request.pageSize = 15;

		JournalLineResponseDTO response = journalService
				.getJournalLineResponse(JournalServiceImpl.SESSIONS_JOURNAL_ID, request);
		assertTrue(response.items.size() == sessionRepository.count() &&
				response.total == response.items.size());
	}

	@Test
	public void testCountAnswerFilter() {
		JournalFilterDTO filter = new JournalFilterDTO();
		filter.value = String.valueOf(4);

		JournalLineRequestDTO request = new JournalLineRequestDTO();
		request.search = "";
		request.filters = new ArrayList<>();
		request.filters.add(filter);
		request.page = 1;
		request.pageSize = 15;

		JournalLineResponseDTO response = journalService
				.getJournalLineResponse(JournalServiceImpl.QUESTIONS_JOURNAL_ID, request);
		assertTrue(response.items
				.stream()
				.allMatch(question ->
						((QuestionDTO)question).answers
								.size() == Integer.parseInt(filter.value)));
	}

	@Test
	public void testStringContainingFilter() {
		JournalLineRequestDTO request = new JournalLineRequestDTO();
		request.search = "мир";
		request.filters = new ArrayList<>();
		request.page = 1;
		request.pageSize = 15;

		JournalLineResponseDTO responseSessions = journalService
				.getJournalLineResponse(JournalServiceImpl.SESSIONS_JOURNAL_ID, request);
		JournalLineResponseDTO responseQuestions = journalService
				.getJournalLineResponse(JournalServiceImpl.QUESTIONS_JOURNAL_ID, request);
		assertTrue(responseSessions.items
				.stream()
				.map(session -> session.name)
				.allMatch(name ->
						name.toLowerCase()
								.contains( request.search.toLowerCase() )));
		assertTrue(responseQuestions.items
				.stream()
				.map(session -> session.name)
				.allMatch(name ->
						name.toLowerCase()
								.contains( request.search.toLowerCase() )));
	}

	@Test
	public void testGenerateQuestions() {
		List<QuestionDTO> questions = sessionService.generateQuestions();
		Set<String> idNewQuestions = questions.stream()
				.map(questionDTO ->
						questionDTO.id).collect(Collectors.toSet());
		assertTrue(idNewQuestions.size() == 5 ||
				idNewQuestions.size() == questionRepository.count());
		assertTrue(questions.stream().
				allMatch(question ->
						question.name
								.equals(questionRepository.findById(Long.parseLong(question.id))
										.orElse(new Question())
										.getName())));
	}

	@Test
	public void testSaveSessionAndSelectedAnswers() {
		SessionCreateDTO request = new SessionCreateDTO();
		request.name = "Родион Амиров";
		request.questionsList = new ArrayList<>();

		List<Question> questions = questionRepository.findAll();
		questions.forEach(question -> {

			AnsweredQuestionDTO answeredQuestion = new AnsweredQuestionDTO();
			answeredQuestion.id = String.valueOf(question.getId());
			answeredQuestion.answersList = new ArrayList<>();

			final boolean[] isFirst = {true};

			answerRepository.findAllByQuestion(question).forEach(answer -> {
				SessionQuestionAnswerDTO questionAnswer = new SessionQuestionAnswerDTO();
				questionAnswer.id = String.valueOf(answer.getId());
				questionAnswer.isSelected = isFirst[0];
				isFirst[0] =false;
				answeredQuestion.answersList.add(questionAnswer);
			});
			request.questionsList.add(answeredQuestion);
		});

		sessionService.createSession(request);

		List<String> idSelectedAnswers = request.questionsList.stream()
				.flatMap(question ->
						question.answersList.stream())
				.filter(answer -> answer.isSelected)
				.map(answer -> answer.id)
				.collect(Collectors.toList());

		assertTrue(sessionRepository.findAll().stream()
				.map(Session::getName)
				.collect(Collectors.toList())
				.contains(request.name));

		assertTrue(selectedAnswerRepository.findAllBySession(
				sessionRepository.findByName(request.name))
				.stream()
				.map(selectedAnswer ->
						String.valueOf(selectedAnswer.getAnswer()
								.getId()))
				.collect(Collectors.toList())
				.containsAll(idSelectedAnswers));
	}

	@Test
	public void testCalculatePoint() {
		SessionCreateDTO request = new SessionCreateDTO();
		request.name = "Родион Амиров";
		request.questionsList = new ArrayList<>();

		List<Question> questions = questionRepository.findAll();
		// Для каждого найденного вопроса в БД
		questions.forEach(question -> {
			// Создаётся DTO вопроса, на который были даны ответы
			AnsweredQuestionDTO answeredQuestion = new AnsweredQuestionDTO();
			answeredQuestion.id = String.valueOf(question.getId());
			answeredQuestion.answersList = new ArrayList<>();

			// Для каждого ответа на текущий вопрос в БД
			// создаётся DTO ответов данных на вопрос.
			// Все они помечаются не выбранными
			answerRepository.findAllByQuestion(question).forEach(answer -> {
				SessionQuestionAnswerDTO questionAnswer = new SessionQuestionAnswerDTO();
				questionAnswer.id = String.valueOf(answer.getId());
				questionAnswer.isSelected = false;
				answeredQuestion.answersList.add(questionAnswer);
			});
			// Первый ответ на вопрос помечается выбранным
			answeredQuestion.answersList.get(0).isSelected = true;
			request.questionsList.add(answeredQuestion);
		});

		// Расчет значения для всех вопросов.
		// В них были выбраны только первые ответы
		assertEquals("72,22", sessionService.createSession(request));
		// Оставляем только первые три вопроса
		request.questionsList = request.questionsList.subList(0,3);
		assertEquals("44,44", sessionService.createSession(request));
		// Выбираются и все вторые ответы
		request.questionsList.forEach(question ->
				question.answersList.get(1).isSelected=true);
		assertEquals("38,89", sessionService.createSession(request));
	}
}
