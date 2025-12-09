package com.miniProjets.quizGame.controller;

import com.miniProjets.quizGame.model.Question;
import com.miniProjets.quizGame.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/questions")
@CrossOrigin(origins = "*")
public class QuestionController {
    @Autowired
    private QuestionService questionService;

    // ADMIN ONLY - Create question
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createQuestion(@RequestBody Question question) {
        try {
            Question createdQuestion = questionService.createQuestion(question);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdQuestion);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ADMIN ONLY - Get question by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getQuestionById(@PathVariable Long id) {
        try {
            Question question = questionService.getQuestionById(id);
            return ResponseEntity.ok(question);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // PUBLIC - Get questions by quiz (needed for taking quizzes)
    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<List<Question>> getQuestionsByQuizId(@PathVariable Long quizId) {
        return ResponseEntity.ok(questionService.getQuestionsByQuizId(quizId));
    }

    // ADMIN ONLY - Update question
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateQuestion(@PathVariable Long id, @RequestBody Question question) {
        try {
            Question updatedQuestion = questionService.updateQuestion(id, question);
            return ResponseEntity.ok(updatedQuestion);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ADMIN ONLY - Delete question
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteQuestion(@PathVariable Long id) {
        try {
            questionService.deleteQuestion(id);
            return ResponseEntity.ok("Question deleted successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}