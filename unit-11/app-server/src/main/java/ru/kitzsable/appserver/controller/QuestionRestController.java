package ru.kitzsable.appserver.controller;

import org.springframework.web.bind.annotation.*;
import ru.kitzsable.appserver.service.QuestionService;
import ru.kitzsable.appserver.transfer.QuestionDTO;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/question")
public class QuestionRestController {

    private final QuestionService questionService;

    public QuestionRestController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PostMapping("create")
    public QuestionDTO createQuestion(@RequestBody QuestionDTO questionDTO) {
        return questionService.save(questionDTO);
    }

    @PutMapping("edit")
    public QuestionDTO editQuestion(@RequestBody QuestionDTO questionDTO) {
        return questionService.update(questionDTO);
    }
}
